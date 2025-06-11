package com.forknowledge.feature.nutrient

fun Long.toMealName(): Int = when(this) {
    0L -> R.string.nutrient_meal_label_breakfast
    1L -> R.string.nutrient_meal_label_lunch
    2L -> R.string.nutrient_meal_label_dinner
    else -> R.string.nutrient_meal_label_snack
}
