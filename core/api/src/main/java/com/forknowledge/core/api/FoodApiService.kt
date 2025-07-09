package com.forknowledge.core.api

import com.forknowledge.core.api.model.GenerateMealPlanResponse
import com.forknowledge.core.api.model.MealPlanResponse
import com.forknowledge.core.api.model.ParseIngredientResponse
import com.forknowledge.core.api.model.RecipeDetailResponse
import com.forknowledge.core.api.model.SearchResponse
import com.forknowledge.core.api.model.UserResponse
import com.forknowledge.core.api.model.post.AnalyzeRecipe
import com.forknowledge.core.api.model.post.ConnectUser
import com.forknowledge.core.api.model.post.MealItem
import kotlinx.serialization.InternalSerializationApi
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
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
        @Query("targetCalories") targetCalories: Int,
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
        @Body meals: List<MealItem>
    ): Response<Unit>

    @DELETE("$API_HEADER_MEAL_PLANNER/{username}/items/{id}")
    suspend fun deleteFromMealPlan(
        @Path("username") username: String,
        @Path("id") mealId: Int,
        @Query("hash") hashKey: String
    ): Response<Unit>

    @DELETE("$API_HEADER_MEAL_PLANNER/{username}/day/{date}")
    suspend fun clearMealPlanDay(
        @Path("username") username: String,
        @Path("date") date: String,
        @Query("hash") hashKey: String
    ): Response<Unit>

    @GET("$API_HEADER_RECIPE/complexSearch")
    suspend fun searchRecipe(
        @Query("offset") index: Int = 0,
        @Query("number") pageSize: Int = 30,
        @Query("query") query: String = "",
        @Query("addRecipeInformation") includeInformation: Boolean,
        @Query("addRecipeNutrition") includeNutrition: Boolean
    ): SearchResponse

    @GET("$API_HEADER_RECIPE/{id}/information")
    suspend fun getRecipeInformation(
        @Path("id") recipeId: Int,
        @Query("includeNutrition") includeNutrition: Boolean = true
    ): Response<RecipeDetailResponse>

    @FormUrlEncoded
    @POST("$API_HEADER_RECIPE/parseIngredients")
    suspend fun parseIngredients(
        @Field("ingredientList") ingredients: String,
        @Query("language") language: String = "en"
    ): Response<ParseIngredientResponse>

    @FormUrlEncoded
    @POST("$API_HEADER_RECIPE/analyzeInstructions")
    suspend fun analyzeInstructions(
        @Field("instructions") instructions: String
    ): Response<AnalyzedInstructionResponse>

    @POST("$API_HEADER_RECIPE/analyze")
    suspend fun analyzeRecipe(
        @Query("includeNutrition") includeNutrition: Boolean = true,
        @Query("language") language: String = "en",
        @Body analyzeRecipe: AnalyzeRecipe
    ): Response<RecipeDetailResponse>
}
