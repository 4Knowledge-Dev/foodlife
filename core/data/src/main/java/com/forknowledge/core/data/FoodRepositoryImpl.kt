package com.forknowledge.core.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.forknowledge.core.api.FoodApiService
import com.forknowledge.core.data.datasource.SearchPagingSource
import com.forknowledge.feature.model.Recipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

const val SEARCH_PAGE_SIZE = 30
const val SEARCH_PREFETCH_DISTANCE = SEARCH_PAGE_SIZE

class FoodRepositoryImpl @Inject constructor(
    private val service: FoodApiService
) : FoodRepository {

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
