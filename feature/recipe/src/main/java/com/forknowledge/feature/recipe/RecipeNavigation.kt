package com.forknowledge.feature.recipe

import com.forknowledge.feature.model.Nutrient
import kotlinx.serialization.Serializable

@Serializable
data class RecipeRoute(
    val recipeId: Int
)

@Serializable
data object NutritionImpactRoute