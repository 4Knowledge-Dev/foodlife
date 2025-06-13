package com.forknowledge.core.api.model.post

data class MealPlanWeek(
    val mealList: List<MealItem>
)

data class MealItem(
    val date: Int,
    val position: Int,
    val slot: Int,
    val type: String,
    val value: Value
)

data class Value(
    val id: Int,
    val image: String,
    val servings: Int,
    val title: String
)
