package com.forknowledge.feature.nutrient

fun Int.toMealName(): Int = when(this) {
    1 -> R.string.nutrient_meal_label_breakfast
    2 -> R.string.nutrient_meal_label_lunch
    3 -> R.string.nutrient_meal_label_dinner
    else -> R.string.nutrient_meal_label_snack
}
