package com.forknowledge.feature.model

data class AddToMealPlanRecipe(
    val dateInMillis: Long,
    val meal: Int,
    val mealPosition: Int = 0,
    val recipeId: Int,
    val recipeName: String,
    val imageUrl: String,
    val servings: Int,
    val cookTime: Int,
    val calories: Int? = null,
    val carbs: Int? = null,
    val protein: Int? = null,
    val fat: Int? = null
)
