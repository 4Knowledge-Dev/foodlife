package com.forknowledge.core.data.model

import com.forknowledge.feature.model.userdata.NutrientData

data class NutritionDisplayData(
    val nutrients : List<NutrientData>? = null,
    val mealCalories: List<Int> = listOf(0, 0, 0, 0),
)
