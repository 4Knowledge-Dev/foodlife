package com.forknowledge.core.data

import androidx.paging.PagingData
import com.forknowledge.feature.model.Recipe
import kotlinx.coroutines.flow.Flow

interface FoodRepository {

    fun searchRecipe(query: String): Flow<PagingData<Recipe>>
}
