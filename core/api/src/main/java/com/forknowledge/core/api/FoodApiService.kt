package com.forknowledge.core.api

import com.forknowledge.core.api.model.post.ConnectUser
import com.forknowledge.core.api.model.GenerateMealPlanResponse
import com.forknowledge.core.api.model.MealPlanResponse
import com.forknowledge.core.api.model.SearchResponse
import com.forknowledge.core.api.model.UserResponse
import com.forknowledge.core.api.model.post.MealPlanWeek
import kotlinx.serialization.InternalSerializationApi
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

const val API_HEADER_USER = "users"
const val API_HEADER_MEAL_PLANNER = "mealplanner"
const val API_HEADER_RECIPE = "recipes"

@OptIn(InternalSerializationApi::class)
interface FoodApiService {

    @POST("$API_HEADER_USER/connect")
    fun connectUser(
        @Body user: ConnectUser
    ): Response<UserResponse>

    @GET("$API_HEADER_MEAL_PLANNER/generate")
    fun generateMealPlan(
        @Query("timeFrame") timeFrame: String = "week",
        @Query("targetCalories") targetCalories: String,
        @Query("diet") diet: String,
        @Query("exclude") excludeIngredients: String
    ): Response<GenerateMealPlanResponse>

    @GET("$API_HEADER_MEAL_PLANNER/{username}/week/{startDate}")
    fun getMealPlan(
        @Path("username") username: String,
        @Path("start-date") startDate: String,
        @Query("hash") hashKey: String = "41ac08626d05b82ff99fd3edc0a95cb9b428db75",
    ): Response<MealPlanResponse>

    @POST("$API_HEADER_MEAL_PLANNER/{username}/items")
    fun addToMealPlan(
        @Path("username") username: String = "16c5330f-3e85-43d3-a8fd-30edf1f8b12a",
        @Query("hash") hashKey: String = "41ac08626d05b82ff99fd3edc0a95cb9b428db75",
        @Body mealPlan: MealPlanWeek
    ): Response<Unit>

    @GET("$API_HEADER_RECIPE/complexSearch")
    suspend fun searchRecipe(
        @Query("query") query: String = "",
        @Query("offset") index: Int = 0,
        @Query("number") pageSize: Int = 30,
        @Query("addRecipeNutrition") includeNutrition: Boolean = true
    ): SearchResponse
}
