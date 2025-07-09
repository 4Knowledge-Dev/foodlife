package com.forknowledge.core.data

import androidx.paging.PagingData
import com.forknowledge.core.api.model.post.ConnectUser
import com.forknowledge.core.common.Result
import com.forknowledge.core.data.model.MealPlanDisplayData
import com.forknowledge.feature.model.AddToMealPlanRecipe
import com.forknowledge.feature.model.Ingredient
import com.forknowledge.feature.model.MealSearchRecipe
import com.forknowledge.feature.model.NutritionSearchRecipe
import com.forknowledge.feature.model.Recipe
import com.forknowledge.feature.model.Step
import com.forknowledge.feature.model.userdata.UserToken
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.InternalSerializationApi

interface FoodRepository {

    @OptIn(InternalSerializationApi::class)
    suspend fun connectUser(user: ConnectUser): Result<UserToken>

    suspend fun generateMealPlan(
        targetCalories: Int,
        diet: String,
        excludeIngredients: String
    ): Flow<List<AddToMealPlanRecipe>>

    suspend fun getMealPlan(
        username: String,
        hashKey: String,
        startDate: String
    ): List<MealPlanDisplayData>

    suspend fun addRecipeToMealPlan(
        username: String,
        hashKey: String,
        recipes: List<AddToMealPlanRecipe>
    )

    suspend fun deleteRecipeFromMealPlan(
        recipeId: Int,
        username: String,
        hashKey: String,
    )

    suspend fun clearMealPlanDay(
        date: String,
        username: String,
        hashKey: String
    )

    fun searchRecipeForNutrition(
        query: String,
        includeInformation: Boolean = false,
        includeNutrition: Boolean = true
    ): Flow<PagingData<NutritionSearchRecipe>>

    fun searchRecipeForMeal(
        query: String,
        includeInformation: Boolean = true,
        includeNutrition: Boolean = true
    ): Flow<PagingData<MealSearchRecipe>>

    fun getRecipeInformation(recipeId: Int): Flow<Recipe?>

    fun parseIngredient(ingredient: String): Flow<Ingredient>

    fun analyzeInstructions(instruction: String): Flow<List<Step>>

    fun analyzeRecipe(
        title: String,
        servings: Int,
        ingredients: List<Ingredient>,
        steps: List<Step>
    ): Flow<Recipe?>
}
