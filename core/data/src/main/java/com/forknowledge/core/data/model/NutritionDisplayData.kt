package com.forknowledge.core.data.model

data class NutritionDisplayData(
    val calories: Long = 0,
    val carbs: Long = 0,
    val proteins: Long = 0,
    val fats: Long = 0,
    val mealCalories: List<Long> = listOf(0, 0, 0, 0)
)
