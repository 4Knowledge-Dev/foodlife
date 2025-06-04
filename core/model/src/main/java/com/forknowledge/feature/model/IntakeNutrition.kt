package com.forknowledge.feature.model

import java.util.Date

data class IntakeNutrition(
    val date: Date? = null,
    val calories: Long = 0,
    val carbs: Long = 0,
    val proteins: Long = 0,
    val fats: Long = 0,
    val breakfast: Meal = Meal(),
    val lunch: Meal = Meal(),
    val dinner: Meal = Meal(),
    val snack: Meal = Meal()
)
