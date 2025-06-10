package com.forknowledge.feature.model

import kotlin.random.Random

data class Recipe(
    val id: Long = Random.nextLong(),
    val name: String = "",
    val imageUrl: String = "",
    val meal: Long = 0,
    val healthScore: Long = 0,
    val nutrients: List<Nutrient> = emptyList(),
)

val logRecipes = listOf<Recipe>(
    Recipe(
        id = 10,
        name = "Spaghetti Bolognese",
        healthScore = 100,
        meal = 0,
        nutrients = listOf(
            Nutrient(
                name = "Calories",
                amount = 1200.0,
                unit = "kcal",
                percentOfDailyNeeds = 0.0
            )
        )
    ),
    Recipe(
        id = 12,
        name = "Spaghetti Bolognese",
        healthScore = 100,
        meal = 0,
        nutrients = listOf(
            Nutrient(
                name = "Calories",
                amount = 1200.0,
                unit = "kcal",
                percentOfDailyNeeds = 0.0
            )
        )
    ),
    Recipe(
        id = 11,
        name = "Spaghetti Bolognese",
        healthScore = 100,
        meal = 0,
        nutrients = listOf(
            Nutrient(
                name = "Calories",
                amount = 1200.0,
                unit = "kcal",
                percentOfDailyNeeds = 0.0
            )
        )
    ),
)
