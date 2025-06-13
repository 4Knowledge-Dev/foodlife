package com.forknowledge.feature.model.userdata

import java.util.Date

data class IntakeNutrition(
    val date: Date? = null,
    val recipes: List<Recipe> = emptyList(),
)
