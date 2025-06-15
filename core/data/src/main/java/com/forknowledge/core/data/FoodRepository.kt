package com.forknowledge.core.data

import androidx.paging.PagingData
import com.forknowledge.core.common.Result
import com.forknowledge.core.data.model.MealPlanDisplayData
import com.forknowledge.feature.model.MealSearchRecipe
import com.forknowledge.feature.model.SearchRecipe
import kotlinx.coroutines.flow.Flow

interface FoodRepository {

    //fun connectUser(user: ConnectUser): Flow<String>

    fun getMealPlan(
        username: String,
        hashKey: String,
        startDate: String
    ): Flow<List<MealPlanDisplayData>>

     suspend fun addRecipeToMealPlan(
        username: String,
        hashKey: String,
        dateInMillis: Long,
        mealPosition: Int,
        recipes: List<MealSearchRecipe>
    ): Result<Unit>

    suspend fun deleteRecipeFromMealPlan(
        recipeId: Int,
        username: String,
        hashKey: String,
    ): Result<Unit>

    fun searchRecipeForNutrition(
        query: String,
        includeInformation: Boolean = false,
        includeNutrition: Boolean = true
    ): Flow<PagingData<SearchRecipe>>

    fun searchRecipeForMeal(
        query: String,
        includeInformation: Boolean = true,
        includeNutrition: Boolean = false
    ): Flow<PagingData<MealSearchRecipe>>
}
