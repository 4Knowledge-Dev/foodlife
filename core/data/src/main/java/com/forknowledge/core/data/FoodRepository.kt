package com.forknowledge.core.data

import androidx.paging.PagingData
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

    /*fun addRecipeToMealPlan(
        username: String,
        hashKey: String,
        recipes: List<Recipe>
    ): Flow<Unit>

    fun deleteRecipeFromMealPlan(
        recipeId: Int,
        username: String,
        hashKey: String,
    ): Flow<Unit>*/

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
