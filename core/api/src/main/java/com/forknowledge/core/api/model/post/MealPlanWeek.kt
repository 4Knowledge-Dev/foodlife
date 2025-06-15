package com.forknowledge.core.api.model.post

import kotlinx.serialization.SerialName

data class MealPlanWeek(
    val mealList: List<MealItem>
)

data class MealItem(
    val date: Int,
    @SerialName("position")
    val meal: Int,
    val slot: Int,
    @SerialName("type")
    val foodType: String,
    @SerialName("value")
    val recipe: MealRecipeItem
)

data class MealRecipeItem(
    val id: Int,
    val image: String,
    val imageType: String,
    val servings: Int,
    val title: String,
    val readyInMinutes: Int,
)
