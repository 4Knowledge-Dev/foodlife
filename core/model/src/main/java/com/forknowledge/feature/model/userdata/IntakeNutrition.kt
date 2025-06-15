package com.forknowledge.feature.model.userdata

import com.forknowledge.feature.model.SearchRecipe
import java.util.Date

data class IntakeNutrition(
    val date: Date? = null,
    val recipes: List<SearchRecipe> = emptyList(),
)
