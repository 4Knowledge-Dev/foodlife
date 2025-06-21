package com.forknowledge.core.data.model

import com.forknowledge.feature.model.userdata.NutrientData
import java.util.Date

data class StatisticsDisplayData(
    val targetCalories: Long = 0,
    val targetCarbs: Float = 0f,
    val targetProteins: Float = 0f,
    val targetFats: Float = 0f,
    val records: List<StatisticsNutrientRecord> = emptyList()
)

data class StatisticsNutrientRecord(
    val date: Date,
    val nutrient: NutrientData
)
