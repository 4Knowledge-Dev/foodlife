package com.forknowledge.feature.recipe

import com.forknowledge.feature.model.Nutrient
import kotlinx.serialization.Serializable

@Serializable
data class RecipeRoute(
    val dateInMillis: Long = 0,
    val mealPosition: Int = 0,
    val recipeId: Int
)

@Serializable
data object NutritionImpactRoute