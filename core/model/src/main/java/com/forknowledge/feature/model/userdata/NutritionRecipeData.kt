package com.forknowledge.feature.model.userdata

data class NutritionRecipeData(
    val id: Long = 0,
    val name: String = "",
    val imageUrl: String = "",
    val mealPosition: Long = 0,
    val servings: Long = 0,
    val calories: Float = 0f
)
