package com.forknowledge.feature.model.userdata

import java.util.Date

data class IntakeNutrition(
    val date: Date = Date(),
    val calories: Long = 0,
    val carbs: Long = 0, // gram
    val proteins: Long = 0, // gram
    val fats: Long = 0, // gram
    val recipes: List<RecipeData> = emptyList(),
)
