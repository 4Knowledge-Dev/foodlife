package com.forknowledge.feature.model.userdata

data class TargetNutrition(
    val calories: Long = 0,
    val carbRatio: Double = 0.0,
    val proteinRatio: Double = 0.0,
    val fatRatio: Double = 0.0,
    val breakfastRatio: Double = 0.25,
    val lunchRatio: Double = 0.35,
    val dinnerRatio: Double = 0.3,
    val snacksRatio: Double = 0.1
)
