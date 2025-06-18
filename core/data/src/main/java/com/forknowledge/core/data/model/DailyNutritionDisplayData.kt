package com.forknowledge.core.data.model

import com.forknowledge.feature.model.userdata.NutrientData

data class DailyNutritionDisplayData(
    val targetCalories: Int,
    val targetCarbRatio: Float,
    val targetFatRatio: Float,
    val targetProteinRatio: Float,
    val nutrients: List<NutrientData>
)
