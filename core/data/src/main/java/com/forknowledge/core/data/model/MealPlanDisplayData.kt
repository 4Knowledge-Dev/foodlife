package com.forknowledge.core.data.model

import com.forknowledge.feature.model.MealRecipe
import java.time.LocalDate

data class MealPlanDisplayData(
    val date: LocalDate,
    val breakfastCalories: Int,
    val lunchCalories: Int,
    val dinnerCalories: Int,
    val breakfast: List<MealRecipe>,
    val lunch: List<MealRecipe>,
    val dinner: List<MealRecipe>
)