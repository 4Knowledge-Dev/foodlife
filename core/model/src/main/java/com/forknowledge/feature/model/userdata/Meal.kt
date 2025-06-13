package com.forknowledge.feature.model.userdata

data class Meal(
    val calories: Long = 0,
    val carbs: Long = 0,
    val proteins: Long = 0,
    val fats: Long = 0,
    val meal: List<Recipe> = emptyList()
)
