package com.forknowledge.core.data

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.forknowledge.core.api.FoodApiService
import com.forknowledge.core.common.extension.toLocalDate
import com.forknowledge.core.data.datasource.FoodDataSource
import com.forknowledge.core.data.datasource.SearchPagingSource
import com.forknowledge.core.data.model.MealPlanDisplayData
import com.forknowledge.feature.model.userdata.Recipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.serialization.InternalSerializationApi
import javax.inject.Inject

const val SEARCH_PAGE_SIZE = 30
const val SEARCH_PREFETCH_DISTANCE = SEARCH_PAGE_SIZE
const val GET_MEAL_PLAN_EXCEPTION = "GetMealPlanException"

@OptIn(InternalSerializationApi::class)
class FoodRepositoryImpl @Inject constructor(
    private val service: FoodApiService,
    private val dataSource: FoodDataSource
) : FoodRepository {

    /*override fun connectUser(user: ConnectUser) = flow {
        emit(service.connectUser(user).hashKey)
    }*/

    override fun getMealPlan(
        username: String,
        hashKey: String,
        startDate: String
    ) = flow {
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
                val breakfast = recipes.filter { it.position == 1 }.map { it.toMealRecipe() }
                val lunch = recipes.filter { it.position == 2 }.map { it.toMealRecipe() }
                val dinner = recipes.filter { it.position == 3 }.map { it.toMealRecipe() }
                val breakfastCalories = mealDay.nutritionSummaryBreakfast.nutrients[6].amount
                val lunchCalories = mealDay.nutritionSummaryLunch.nutrients[6].amount
                val dinnerCalories = mealDay.nutritionSummaryDinner.nutrients[6].amount
                mealPlan.add(
                    MealPlanDisplayData(
                        date = date,
                        breakfastCalories = breakfastCalories.toInt(),
                        lunchCalories = lunchCalories.toInt(),
                        dinnerCalories = dinnerCalories.toInt(),
                        breakfast = breakfast,
                        lunch = lunch,
                        dinner = dinner
                    )
                )
            }
            emit(mealPlan)
        } else {
            Log.e(GET_MEAL_PLAN_EXCEPTION, "Fail on getMealPlan: ${response.errorBody()}")
            emit(emptyList<MealPlanDisplayData>())
        }
    }

    override fun searchRecipe(query: String): Flow<PagingData<Recipe>> {
        return Pager(
            config = PagingConfig(
                pageSize = SEARCH_PAGE_SIZE,
                enablePlaceholders = false,
                maxSize = SEARCH_PAGE_SIZE + (2 * SEARCH_PREFETCH_DISTANCE)
            ),
            pagingSourceFactory = { SearchPagingSource(service, query) }
        )
            .flow
            .flowOn(Dispatchers.IO)
    }
}
