package com.forknowledge.feature.model

data class Recipe(
    val recipeId: Int,
    val imageUrl: String,
    val recipeName: String,
    val summary: String,
    val readyInMinutes: Int,
    val preparationMinutes: Int?,
    val cookingMinutes: Int?,
    val servings: Int,
    val sourceName: String,
    val sourceUrl: String,
    val healthScore: Float,
    val ingredients: List<Ingredient>
)

data class Ingredient(
    val ingredientId: Int,
    val ingredientName: String,
    val imageUrl: String,
    val originalMeasures: Measure,
    val metricMeasures: Measure,
    val usMeasures: Measure
)

data class Measure(
    val amount: Float,
    val unit: String
)
