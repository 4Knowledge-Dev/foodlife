package com.forknowledge.core.data.datasource

import com.forknowledge.core.api.FoodApiService
import com.forknowledge.core.api.model.post.ConnectUser
import com.forknowledge.core.api.model.post.MealItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.InternalSerializationApi
import javax.inject.Inject

@OptIn(InternalSerializationApi::class)
class FoodDataSource @Inject constructor(
    private val apiService: FoodApiService
) {

    suspend fun connectUser(user: ConnectUser) = withContext(Dispatchers.IO) {
        apiService.connectUser(user)
    }

    suspend fun generateMealPlan(
        targetCalories: String,
        diet: String,
        excludeIngredients: String
    ) = withContext(Dispatchers.IO) {
        apiService.generateMealPlan(
            targetCalories = targetCalories,
            diet = diet,
            excludeIngredients = excludeIngredients
        )
    }

    suspend fun getMealPlan(
        username: String,
        hashKey: String,
        startDate: String
    ) = withContext(Dispatchers.IO) {
        apiService.getMealPlan(
            username = username,
            hashKey = hashKey,
            startDate = startDate
        )
    }

    suspend fun addToMealPlan(
        username: String,
        hashKey: String,
        mealList: List<MealItem>
    ) = withContext(Dispatchers.IO) {
        apiService.addToMealPlan(
            username = username,
            hashKey = hashKey,
            meals = mealList
        )
    }

    suspend fun deleteFromMealPlan(
        username: String,
        hashKey: String,
        mealId: Int,
    ) = withContext(Dispatchers.IO) {
        apiService.deleteFromMealPlan(
            username = username,
            hashKey = hashKey,
            mealId = mealId,
        )
    }

    suspend fun getRecipeInformation(recipeId: Int) = withContext(Dispatchers.IO) {
        apiService.getRecipeInformation(recipeId)
    }
}
