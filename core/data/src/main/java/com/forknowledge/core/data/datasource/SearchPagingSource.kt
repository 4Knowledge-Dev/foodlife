package com.forknowledge.core.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.forknowledge.core.api.FoodApiService
import com.forknowledge.core.data.SEARCH_PAGE_SIZE
import com.forknowledge.feature.model.userdata.Recipe
import kotlinx.serialization.InternalSerializationApi
import okio.IOException
import retrofit2.HttpException

@OptIn(InternalSerializationApi::class)
class SearchPagingSource(
    val service: FoodApiService,
    val query: String,
) : PagingSource<Int, Recipe>() {

    override fun getRefreshKey(state: PagingState<Int, Recipe>): Int? {
        // Try to find the page key (offset) of the closest page to anchorPosition from
        // either the prevKey or the nextKey; you need to handle nullability
        // here.
        //  * prevKey == null -> anchorPage is the first page.
        //  * nextKey == null -> anchorPage is the last page.
        //  * both prevKey and nextKey are null -> anchorPage is the
        //    initial page, so return null.
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(state.config.pageSize)
                ?: anchorPage?.nextKey?.minus(state.config.pageSize)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Recipe> {
        try {
            val position = params.key ?: 0
            val response = service.searchRecipe(
                query = query,
                index = position,
                pageSize = params.loadSize
            )
            return LoadResult.Page(
                data = response.recipes.map { it.toRecipe() },
                prevKey = if (position == 0) null else position - SEARCH_PAGE_SIZE,
                nextKey = position + params.loadSize
            )
        } catch (e: IOException) {
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            return LoadResult.Error(e)
        }
    }
}
