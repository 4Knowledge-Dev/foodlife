package com.forknowledge.core.api

import com.forknowledge.core.api.model.SearchResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.InternalSerializationApi
import retrofit2.http.GET
import retrofit2.http.Query

const val API_HEADER_RECIPE = "recipes"

interface FoodApiService {

    @OptIn(InternalSerializationApi::class)
    @GET("$API_HEADER_RECIPE/complexSearch")
    fun searchRecipe(
        @Query("number") pageSize: Int = 30,
        @Query("offset") index: Int = 0,
        @Query("query") query: String = "",
    ): Flow<SearchResponse>
}