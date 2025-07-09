package com.forknowledge.core.data

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.forknowledge.core.api.FoodApiService
import com.forknowledge.core.api.getImageUrl
import com.forknowledge.core.api.model.NutrientResponse
import com.forknowledge.core.api.model.post.ConnectUser
import com.forknowledge.core.api.model.post.MealItem
import com.forknowledge.core.api.model.post.MealRecipeItem
import com.forknowledge.core.common.AppConstant.NUTRIENT_CALORIES_NAME
import com.forknowledge.core.common.AppConstant.NUTRIENT_CARB_NAME
import com.forknowledge.core.common.AppConstant.NUTRIENT_FAT_NAME
import com.forknowledge.core.common.AppConstant.NUTRIENT_PROTEIN_NAME
import com.forknowledge.core.common.Result
import com.forknowledge.core.common.extension.toEpochSeconds
import com.forknowledge.core.common.extension.toLocalDate
import com.forknowledge.core.common.getCurrentWeekDays
import com.forknowledge.core.data.datasource.FoodDataSource
import com.forknowledge.core.data.datasource.SearchPagingSource
import com.forknowledge.core.data.model.MealPlanDisplayData
import com.forknowledge.feature.model.AddToMealPlanRecipe
import com.forknowledge.feature.model.MealSearchRecipe
import com.forknowledge.feature.model.NutritionSearchRecipe
import com.forknowledge.feature.model.userdata.UserToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.serialization.InternalSerializationApi
import javax.inject.Inject
import kotlin.math.roundToInt

const val API_MEAL_TYPE_RECIPE = "RECIPE"
const val SEARCH_PAGE_SIZE = 30
const val SEARCH_PREFETCH_DISTANCE = SEARCH_PAGE_SIZE
const val GET_MEAL_PLAN_EXCEPTION = "GetMealPlanException"
const val CONNECT_USER_EXCEPTION = "ConnectUserException"
const val GET_RECIPE_EXCEPTION = "GetRecipeException"

@OptIn(InternalSerializationApi::class)
class FoodRepositoryImpl @Inject constructor(
    private val service: FoodApiService,
    private val dataSource: FoodDataSource
) : FoodRepository {

    override suspend fun connectUser(user: ConnectUser): Result<UserToken> {
        return try {
            val response = dataSource.connectUser(user)
            if (response.isSuccessful && response.body() != null) {
                Result.Success(response.body()!!.toUserToken())
            } else {
                Result.Success(UserToken("", ""))
            }
        } catch (e: Exception) {
            Log.e(CONNECT_USER_EXCEPTION, "Connect user to server failed with ", e)
            Result.Error(e)
        }
    }

    override suspend fun generateMealPlan(
        targetCalories: Int,
        diet: String,
        excludeIngredients: String
    ) = flow {
        val response = dataSource.generateMealPlan(
            targetCalories = targetCalories,
            diet = diet,
            excludeIngredients = excludeIngredients
        )
        if (response.isSuccessful && response.body() != null) {
            val mealPlan = response.body()!!.week.toDayPlanResponseList()
            val recipes = mutableListOf<AddToMealPlanRecipe>()
            val dates = getCurrentWeekDays()
            mealPlan.forEachIndexed { index, mealDay ->
                recipes.addAll(
                    mealDay.meals.map { meal ->
                        AddToMealPlanRecipe(
                            dateInMillis = dates[index].toEpochSeconds(),
                            meal = mealDay.meals.indexOf(meal) + 1,
                            recipeId = meal.id,
                            recipeName = meal.title,
                            imageUrl = getImageUrl(
                                id = meal.id.toString(),
                                imageType = meal.imageType,
                                mealType = API_MEAL_TYPE_RECIPE
                            ),
                            servings = meal.servings,
                            cookTime = meal.readyInMinutes
                        )
                    }
                )
            }
            emit(recipes)
        } else {
            emit(emptyList())
        }
    }

    override suspend fun getMealPlan(
        username: String,
        hashKey: String,
        startDate: String
    ): List<MealPlanDisplayData> {
        val response = dataSource.getMealPlan(
            username = username,
            hashKey = hashKey,
            startDate = startDate
        )
        val mealPlan: MutableList<MealPlanDisplayData> = mutableListOf()
        if (response.isSuccessful && response.body() != null) {
            val mealDays = response.body()!!.days
            mealDays.forEach { mealDay ->
                val date = mealDay.date.toLocalDate()

                // Total nutrients of the day
                val nutritionSummary = mealDay.nutritionSummary.nutrients
                val breakfastSummary = mealDay.nutritionSummaryBreakfast.nutrients
                val lunchSummary = mealDay.nutritionSummaryLunch.nutrients
                val dinnerSummary = mealDay.nutritionSummaryDinner.nutrients

                val nutrition = listOf(
                    nutritionSummary.getNutrientByName(NUTRIENT_CALORIES_NAME),
                    nutritionSummary.getNutrientByName(NUTRIENT_CARB_NAME),
                    nutritionSummary.getNutrientByName(NUTRIENT_PROTEIN_NAME),
                    nutritionSummary.getNutrientByName(NUTRIENT_FAT_NAME)
                )
                val breakfastNutrition = listOf(
                    breakfastSummary.getNutrientByName(NUTRIENT_CALORIES_NAME),
                    breakfastSummary.getNutrientByName(NUTRIENT_CARB_NAME),
                    breakfastSummary.getNutrientByName(NUTRIENT_PROTEIN_NAME),
                    breakfastSummary.getNutrientByName(NUTRIENT_FAT_NAME)
                )
                val lunchNutrition = listOf(
                    lunchSummary.getNutrientByName(NUTRIENT_CALORIES_NAME),
                    lunchSummary.getNutrientByName(NUTRIENT_CARB_NAME),
                    lunchSummary.getNutrientByName(NUTRIENT_PROTEIN_NAME),
                    lunchSummary.getNutrientByName(NUTRIENT_FAT_NAME)
                )
                val dinnerNutrition = listOf(
                    dinnerSummary.getNutrientByName(NUTRIENT_CALORIES_NAME),
                    dinnerSummary.getNutrientByName(NUTRIENT_CARB_NAME),
                    dinnerSummary.getNutrientByName(NUTRIENT_PROTEIN_NAME),
                    dinnerSummary.getNutrientByName(NUTRIENT_FAT_NAME)
                )

                // Filter recipes by slot
                val recipes = mealDay.items
                val breakfast = recipes.filter { it.slot == 1 }.map { it.toMealRecipe() }
                val lunch = recipes.filter { it.slot == 2 }.map { it.toMealRecipe() }
                val dinner = recipes.filter { it.slot == 3 }.map { it.toMealRecipe() }
                mealPlan.add(
                    MealPlanDisplayData(
                        date = date,
                        nutritionSummary = nutrition.map { it.amount.roundToInt() },
                        breakfastNutrition = breakfastNutrition.map { it.amount.roundToInt() },
                        lunchNutrition = lunchNutrition.map { it.amount.roundToInt() },
                        dinnerNutrition = dinnerNutrition.map { it.amount.roundToInt() },
                        breakfast = breakfast,
                        lunch = lunch,
                        dinner = dinner
                    )
                )
            }
        }
        return mealPlan
    }

    private fun List<NutrientResponse>.getNutrientByName(name: String): NutrientResponse {
        return this.firstOrNull { it.name == name } ?: NutrientResponse(
            name = name,
            amount = 0f,
            unit = "",
            percentOfDailyNeeds = 0f
        )
    }

    override suspend fun addRecipeToMealPlan(
        username: String,
        hashKey: String,
        recipes: List<AddToMealPlanRecipe>
    ) {
        val meals = recipes.map { recipe ->
            MealItem(
                date = recipe.dateInMillis,
                slot = recipe.meal,
                position = recipe.mealPosition,
                type = API_MEAL_TYPE_RECIPE,
                recipe = MealRecipeItem(
                    id = recipe.recipeId,
                    title = recipe.recipeName,
                    image = recipe.imageUrl,
                    servings = recipe.servings,
                    readyInMinutes = recipe.cookTime,
                    calories = recipe.calories,
                    carbs = recipe.carbs,
                    protein = recipe.protein,
                    fat = recipe.fat
                )
            )
        }
        dataSource.addToMealPlan(
            username = username,
            hashKey = hashKey,
            mealList = meals
        )
    }

    override suspend fun deleteRecipeFromMealPlan(
        recipeId: Int,
        username: String,
        hashKey: String
    ) {
        dataSource.deleteFromMealPlan(
            username = username,
            hashKey = hashKey,
            mealId = recipeId
        )
    }

    override suspend fun clearMealPlanDay(
        date: String,
        username: String,
        hashKey: String
    ) {
        dataSource.clearMealPlanDay(
            date = date,
            username = username,
            hashKey = hashKey
        )
    }

    override fun searchRecipeForNutrition(
        query: String,
        includeInformation: Boolean,
        includeNutrition: Boolean
    ): Flow<PagingData<NutritionSearchRecipe>> {
        return Pager(
            config = PagingConfig(
                pageSize = SEARCH_PAGE_SIZE,
                enablePlaceholders = false,
                maxSize = SEARCH_PAGE_SIZE + (2 * SEARCH_PREFETCH_DISTANCE)
            ),
            pagingSourceFactory = {
                SearchPagingSource(
                    service = service,
                    query = query,
                    includeInformation = includeInformation,
                    includeNutrition = includeNutrition
                )
            }
        )
            .flow
            .map { pagingData ->
                pagingData.map { it.toNutritionSearchRecipe() }
            }
            .flowOn(Dispatchers.IO)
    }

    override fun searchRecipeForMeal(
        query: String,
        includeInformation: Boolean,
        includeNutrition: Boolean
    ): Flow<PagingData<MealSearchRecipe>> {
        return Pager(
            config = PagingConfig(
                pageSize = SEARCH_PAGE_SIZE,
                enablePlaceholders = false,
                maxSize = SEARCH_PAGE_SIZE + (2 * SEARCH_PREFETCH_DISTANCE)
            ),
            pagingSourceFactory = {
                SearchPagingSource(
                    service = service,
                    query = query,
                    includeInformation = includeInformation,
                    includeNutrition = includeNutrition
                )
            }
        )
            .flow
            .map { pagingData ->
                pagingData.map { it.toMealSearchRecipe() }
            }
            .flowOn(Dispatchers.IO)
    }

    override fun getRecipeInformation(recipeId: Int) = flow {
        val response = dataSource.getRecipeInformation(recipeId)
        if (response.isSuccessful && response.body() != null) {
            emit(response.body()!!.toRecipe())
        } else {
            emit(null)
        }
    }

    override fun parseIngredient(ingredient: String) = flow {
        val response = dataSource.parseIngredients(ingredient)
        if (response.isSuccessful && response.body() != null) {
            emit(response.body()!!.first().toIngredient())
        } else {
            throw IllegalStateException()
        }
    }

    override fun analyzeInstructions(instruction: String) = flow {
        val response = dataSource.analyzeInstructions(instruction)
        if (response.isSuccessful && response.body() != null) {
            emit(response.body()!!.instruction.first().steps.map { it.toInstruction() })
        } else {
            throw IllegalStateException()
        }
    }
}
