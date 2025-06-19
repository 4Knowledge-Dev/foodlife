package com.forknowledge.feature.model.userdata

import java.util.Date

data class IntakeNutrition(
    val date: Date = Date(),
    val nutrients: List<NutrientData> = emptyList(),
    val recipes: List<RecipeData> = emptyList(),
)
