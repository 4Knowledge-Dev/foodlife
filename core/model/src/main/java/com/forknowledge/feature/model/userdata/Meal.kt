package com.forknowledge.feature.model.userdata

import com.forknowledge.feature.model.SearchRecipe

data class Meal(
    val calories: Long = 0,
    val carbs: Long = 0,
    val proteins: Long = 0,
    val fats: Long = 0,
    val meal: List<SearchRecipe> = emptyList()
)
