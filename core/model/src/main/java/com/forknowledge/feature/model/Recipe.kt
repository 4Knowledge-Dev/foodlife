package com.forknowledge.feature.model

data class Recipe(
    val recipeId: Int,
    val imageUrl: String,
    val recipeName: String,
    val summary: String,
    val readyInMinutes: Int,
    val preparationMinutes: Int,
    val cookingMinutes: Int,
    val servings: Int,
    val sourceName: String,
    val sourceUrl: String,
    val healthScore: Float,
    val ingredients: List<Ingredient>,
    val steps: List<Step>
)

data class Ingredient(
    val ingredientId: Int,
    val ingredientName: String,
    val imageUrl: String,
    val originalMeasures: Measure = Measure(0f, ""),
    val metricMeasures: Measure = Measure(0f, ""),
    val usMeasures: Measure = Measure(0f, "")
)

data class Measure(
    val amount: Float,
    val unit: String
)

data class Step(
    val stepNumber: Int,
    val description: String,
    val equipments: List<Equipment>,
    val ingredients: List<Ingredient>
)

data class Equipment(
    val equipmentId: Int,
    val equipmentName: String,
    val imageUrl: String
)
