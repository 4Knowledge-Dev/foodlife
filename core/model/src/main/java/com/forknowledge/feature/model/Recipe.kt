package com.forknowledge.feature.model

import com.forknowledge.feature.model.userdata.LogRecipe
import kotlin.random.Random

data class Recipe(
    val recipeId: Int = Random.nextInt(10000, Int.MAX_VALUE),
    val imageUrl: String = "",
    val recipeName: String = "",
    val summary: String = "",
    val readyInMinutes: Int = 0,
    val preparationMinutes: Int = 0,
    val cookingMinutes: Int = 0,
    val servings: Int = 0,
    val sourceName: String = "",
    val sourceUrl: String = "",
    val healthScore: Int = 0,
    val nutrition: List<Nutrient> = emptyList(),
    val properties: List<Property> = emptyList(),
    val ingredients: List<Ingredient> = emptyList(),
    val steps: List<Step> = emptyList()
) {
    fun toLogRecipe() = LogRecipe(
        id = recipeId,
        name = recipeName,
        imageUrl = imageUrl,
        servings = servings,
        nutrients = nutrition
    )
}

data class Ingredient(
    val ingredientId: Int = Random.nextInt(),
    val ingredientName: String = "",
    val imageUrl: String = "",
    val originalMeasures: Measure = Measure(0f, ""),
    val metricMeasures: Measure = Measure(0f, ""),
    val usMeasures: Measure = Measure(0f, "")
)

data class Measure(
    val amount: Float = 0f,
    val unit: String = ""
)

data class Step(
    val stepNumber: Int = 1,
    val description: String = "",
    val equipments: List<Equipment> = emptyList(),
    val ingredients: List<Ingredient> = emptyList()
)

data class Equipment(
    val equipmentId: Int = Random.nextInt(10000, Int.MAX_VALUE),
    val equipmentName: String = "",
    val imageUrl: String = ""
)

data class Property(
    val name: String = "",
    val value: Int = 0
)
