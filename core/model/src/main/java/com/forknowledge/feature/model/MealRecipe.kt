package com.forknowledge.feature.model

data class MealRecipe(
    val mealId: Int,
    val recipeId: Int,
    val imageUrl: String,
    val name: String,
    val servings: Int,
    val cookTime: Int
)
