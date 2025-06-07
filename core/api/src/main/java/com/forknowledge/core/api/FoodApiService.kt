package com.forknowledge.core.api

import com.forknowledge.core.api.model.SearchResponse
import kotlinx.serialization.InternalSerializationApi
import retrofit2.http.GET
import retrofit2.http.Query

const val API_HEADER_RECIPE = "recipes"

interface FoodApiService {

    @OptIn(InternalSerializationApi::class)
    @GET("$API_HEADER_RECIPE/complexSearch")
    suspend fun searchRecipe(
        @Query("query") query: String = "",
        @Query("offset") index: Int = 0,
        @Query("number") pageSize: Int = 30,
        @Query("addRecipeNutrition") includeNutrition: Boolean = true
    ): SearchResponse
}
