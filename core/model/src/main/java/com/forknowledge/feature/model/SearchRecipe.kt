package com.forknowledge.feature.model

import com.forknowledge.feature.model.userdata.LogRecipe
import kotlin.math.roundToInt

const val NUTRIENT_CALORIES_NAME = "Calories"
const val NUTRIENT_CARB_NAME = "Carbohydrates"
const val NUTRIENT_PROTEIN_NAME = "Protein"
const val NUTRIENT_FAT_NAME = "Fat"

data class SearchRecipe(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val servings: Int,
    val cookTime: Int,
    val nutrients: List<Nutrient>
) {

    fun toNutritionSearchRecipe() = NutritionSearchRecipe(
        id = id,
        name = name,
        imageUrl = imageUrl,
        servings = servings,
        nutrients = nutrients
    )

    fun toMealSearchRecipe(): MealSearchRecipe {
        val nutrients = listOf(
            nutrients.firstOrNull { it.name == NUTRIENT_CALORIES_NAME }?.amount ?: 0f,
            nutrients.firstOrNull { it.name == NUTRIENT_CARB_NAME }?.amount ?: 0f,
            nutrients.firstOrNull { it.name == NUTRIENT_PROTEIN_NAME }?.amount ?: 0f,
            nutrients.firstOrNull { it.name == NUTRIENT_FAT_NAME }?.amount ?: 0f
        )
        return MealSearchRecipe(
            id = id,
            name = name,
            imageUrl = imageUrl,
            servings = servings,
            cookTime = cookTime,
            nutrients = nutrients.map { it.roundToInt() }
        )
    }
}

data class NutritionSearchRecipe(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val servings: Int,
    val nutrients: List<Nutrient>
) {
    fun toLogRecipe() = LogRecipe(
        id = id,
        name = name,
        imageUrl = imageUrl,
        servings = servings,
        nutrients = nutrients
    )
}

data class MealSearchRecipe(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val servings: Int,
    val cookTime: Int,
    val nutrients: List<Int>
)

val logRecipes = listOf(
    SearchRecipe(
        id = 10,
        name = "Spaghetti Bolognese",
        imageUrl = "",
        servings = 2,
        cookTime = 10,
        nutrients = listOf(
            Nutrient(
                name = "Calories",
                amount = 1200f,
                unit = "kcal",
                dailyPercentValue = 5
            )
        )
    )
)
