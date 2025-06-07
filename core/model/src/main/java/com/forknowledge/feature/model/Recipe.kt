@file:JvmName("RecipeKt")

package com.forknowledge.feature.model

import kotlin.random.Random

data class Recipe(
    val id: Long = Random.nextLong(),
    val name: String = "",
    val imageUrl: String = "",
    val calories: Long = 0,
    val healthScore: Long = 0
)

val logRecipes = listOf<Recipe>(
    Recipe(
        name = "Spaghetti Bolognese",
        calories = 500,
        healthScore = 100
    ),
    Recipe(
        name = "Spaghetti Bolognese",
        calories = 500,
        healthScore = 100
    )
)
