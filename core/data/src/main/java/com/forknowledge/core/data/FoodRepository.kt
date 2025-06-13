package com.forknowledge.core.data

import androidx.paging.PagingData
import com.forknowledge.core.api.model.post.ConnectUser
import com.forknowledge.feature.model.userdata.Recipe
import kotlinx.coroutines.flow.Flow

interface FoodRepository {

    fun connectUser(user: ConnectUser): Flow<String>

    fun generateMealPlan(
        targetCalories: String,
        diet: String,
        excludeIngredients: String
    ): Flow<String>

    fun searchRecipe(query: String): Flow<PagingData<Recipe>>
}
