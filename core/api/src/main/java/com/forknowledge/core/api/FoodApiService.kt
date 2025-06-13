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
    suspend fun connectUser(
        @Body user: ConnectUser
    ): Response<UserResponse>

    @GET("$API_HEADER_MEAL_PLANNER/generate")
    suspend fun generateMealPlan(
        @Query("timeFrame") timeFrame: String = "week",
        @Query("targetCalories") targetCalories: String,
        @Query("diet") diet: String,
        @Query("exclude") excludeIngredients: String
    ): Response<GenerateMealPlanResponse>

    @GET("$API_HEADER_MEAL_PLANNER/{username}/week/{start-date}")
    suspend fun getMealPlan(
        @Path("username") username: String,
        @Path("start-date") startDate: String,
        @Query("hash") hashKey: String,
    ): Response<MealPlanResponse>

    @POST("$API_HEADER_MEAL_PLANNER/{username}/items")
    suspend fun addToMealPlan(
        @Path("username") username: String,
        @Query("hash") hashKey: String,
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
