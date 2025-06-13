package com.forknowledge.core.data

import androidx.paging.PagingData
import com.forknowledge.core.api.model.post.ConnectUser
import com.forknowledge.core.data.model.MealPlanDisplayData
import com.forknowledge.feature.model.userdata.Recipe
import kotlinx.coroutines.flow.Flow

interface FoodRepository {

    //fun connectUser(user: ConnectUser): Flow<String>

    fun getMealPlan(
        username: String,
        hashKey: String,
        startDate: String
    ): Flow<List<MealPlanDisplayData>>

    fun searchRecipe(query: String): Flow<PagingData<Recipe>>
}
