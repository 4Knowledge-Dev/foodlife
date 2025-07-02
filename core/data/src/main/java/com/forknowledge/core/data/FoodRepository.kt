package com.forknowledge.core.data

import androidx.paging.PagingData
import com.forknowledge.core.api.model.post.ConnectUser
import com.forknowledge.core.common.Result
import com.forknowledge.core.data.model.MealPlanDisplayData
import com.forknowledge.feature.model.MealSearchRecipe
import com.forknowledge.feature.model.NutritionSearchRecipe
import com.forknowledge.feature.model.Recipe
import com.forknowledge.feature.model.userdata.UserToken
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.InternalSerializationApi

interface FoodRepository {

    @OptIn(InternalSerializationApi::class)
    suspend fun connectUser(user: ConnectUser): Result<UserToken>

    suspend fun getMealPlan(
        username: String,
        hashKey: String,
        startDate: String
    ): List<MealPlanDisplayData>

    suspend fun addRecipeToMealPlan(
        username: String,
        hashKey: String,
        dateInMillis: Long,
        mealSlot: Int,
        mealPosition: Int = 0,
        recipes: List<MealSearchRecipe>
    )

    suspend fun deleteRecipeFromMealPlan(
        recipeId: Int,
        username: String,
        hashKey: String,
    )

    fun searchRecipeForNutrition(
        query: String,
        includeInformation: Boolean = false,
        includeNutrition: Boolean = true
    ): Flow<PagingData<NutritionSearchRecipe>>

    fun searchRecipeForMeal(
        query: String,
        includeInformation: Boolean = true,
        includeNutrition: Boolean = false
    ): Flow<PagingData<MealSearchRecipe>>

    fun getRecipeInformation(recipeId: Int): Flow<Recipe?>
}
