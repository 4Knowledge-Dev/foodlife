package com.forknowledge.feature.model.userdata

import com.forknowledge.feature.model.Nutrient

data class LogRecipe(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val servings: Int,
    val nutrients: List<Nutrient>
)
