package com.forknowledge.feature.model

import com.forknowledge.feature.model.userdata.Nutrient

data class SearchRecipe(
    val id: Long,
    val name: String,
    val imageUrl: String,
    val meal: Long = 0, // will remove later
    val healthScore: Int,
    val servings: Int,
    val cookTime: Int,
    val nutrients: List<Nutrient>
){
    fun toMealSearchRecipe() = MealSearchRecipe(
        id = id,
        name = name,
        imageUrl = imageUrl,
        servings = servings,
        cookTime = cookTime
    )
}

data class MealSearchRecipe(
    val id: Long,
    val name: String,
    val imageUrl: String,
    val servings: Int,
    val cookTime: Int
)

val logRecipes = listOf<SearchRecipe>(
    SearchRecipe(
        id = 10,
        name = "Spaghetti Bolognese",
        healthScore = 100,
        meal = 0,
        imageUrl = "",
        servings = 2,
        cookTime = 10,
        nutrients = listOf(
            Nutrient(
                name = "Calories",
                amount = 1200.0,
                unit = "kcal",
            )
        )
    )
)
