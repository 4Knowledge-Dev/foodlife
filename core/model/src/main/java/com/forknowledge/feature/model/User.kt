package com.forknowledge.feature.model

data class User(
    val targetNutrition: TargetNutrition = TargetNutrition(),
    val mealRecords: List<IntakeNutrition> = emptyList()
)
