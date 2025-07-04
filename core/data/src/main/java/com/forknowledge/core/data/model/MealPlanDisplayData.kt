package com.forknowledge.core.data.model

import com.forknowledge.feature.model.MealRecipe
import java.time.LocalDate

data class MealPlanDisplayData(
    val date: LocalDate,
    val nutritionSummary: List<Int>, // done
    val breakfastNutrition: List<Int>,
    val lunchNutrition: List<Int>,
    val dinnerNutrition: List<Int>,
    val breakfast: List<MealRecipe>,
    val lunch: List<MealRecipe>,
    val dinner: List<MealRecipe>
)