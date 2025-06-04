package com.forknowledge.feature.model

data class TargetNutrition(
    val calories: Long = 0,
    val carbs: Long = 0,
    val proteins: Long = 0,
    val fats: Long = 0,
    val breakfastCalories: Long = 0,
    val lunchCalories: Long = 0,
    val dinnerCalories: Long = 0,
    val snackCalories: Long = 0
)
