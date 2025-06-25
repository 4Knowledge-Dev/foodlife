package com.forknowledge.core.data

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.forknowledge.core.api.FoodApiService
import com.forknowledge.core.api.model.post.ConnectUser
import com.forknowledge.core.api.model.post.MealItem
import com.forknowledge.core.api.model.post.MealRecipeItem
import com.forknowledge.core.common.Result
import com.forknowledge.core.common.extension.toLocalDate
import com.forknowledge.core.data.datasource.FoodDataSource
import com.forknowledge.core.data.datasource.SearchPagingSource
import com.forknowledge.core.data.model.MealPlanDisplayData
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
                val recipes = mealDay.items
                val breakfast = recipes.filter { it.slot == 1 }.map { it.toMealRecipe() }
                val lunch = recipes.filter { it.slot == 2 }.map { it.toMealRecipe() }
                val dinner = recipes.filter { it.slot == 3 }.map { it.toMealRecipe() }
                mealPlan.add(
                    MealPlanDisplayData(
                        date = date,
                        breakfast = breakfast,
                        lunch = lunch,
                        dinner = dinner
                    )
                )
            }
        }
        return mealPlan
    }

    override suspend fun addRecipeToMealPlan(
        username: String,
        hashKey: String,
        dateInMillis: Long,
        mealPosition: Int,
        recipes: List<MealSearchRecipe>
    ) {
        val meals = recipes.map { recipe ->
            MealItem(
                date = dateInMillis,
                slot = mealPosition,
                position = 0,
                type = "RECIPE",
                recipe = MealRecipeItem(
                    id = recipe.id,
                    title = recipe.name,
                    image = recipe.imageUrl,
                    servings = recipe.servings,
                    readyInMinutes = recipe.cookTime,
                    isComplete = false
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
}
