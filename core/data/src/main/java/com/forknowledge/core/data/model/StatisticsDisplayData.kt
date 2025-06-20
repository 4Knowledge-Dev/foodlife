package com.forknowledge.core.data.model

import com.forknowledge.feature.model.userdata.IntakeNutrition

data class StatisticsDisplayData(
    val targetCalories: Int,
    val targetCarbs: Int,
    val targetProteins: Int,
    val targetFats: Int,
    val records: List<IntakeNutrition>
)
