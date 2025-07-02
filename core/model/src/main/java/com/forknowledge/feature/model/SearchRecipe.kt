package com.forknowledge.feature.model

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

    fun toMealSearchRecipe() = MealSearchRecipe(
        id = id,
        name = name,
        imageUrl = imageUrl,
        servings = servings,
        cookTime = cookTime
    )
}

data class NutritionSearchRecipe(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val servings: Int,
    val nutrients: List<Nutrient>
)

data class MealSearchRecipe(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val servings: Int,
    val cookTime: Int
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
