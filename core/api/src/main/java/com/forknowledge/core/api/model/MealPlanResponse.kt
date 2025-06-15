package com.forknowledge.core.api.model

import com.forknowledge.core.api.getImageUrl
import com.forknowledge.feature.model.MealRecipe
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@InternalSerializationApi
@Serializable
data class MealPlanResponse(
    val days: List<DayResponse>
)

@InternalSerializationApi
@Serializable
data class DayResponse(
    val date: Long,
    val day: String,
    val items: List<FoodResponse>,
    val nutritionSummaryBreakfast: NutritionResponse,
    val nutritionSummaryDinner: NutritionResponse,
    val nutritionSummaryLunch: NutritionResponse
)

@InternalSerializationApi
@Serializable
data class FoodResponse(
    val id: Int,
    val slot: Int,
    val type: String? = null,
    val value: ValueResponse
) {
    fun toMealRecipe() = MealRecipe(
        mealId = id,
        recipeId = value.id,
        imageUrl = getImageUrl(
            id = value.id.toString(),
            image = value.image ?: "",
            mealType = type ?: "",
            imageType = value.imageType ?: ""
        ),
        name = value.title,
        servings = value.servings,
        cookTime = value.readyInMinutes
    )
}

@InternalSerializationApi
@Serializable
data class ValueResponse(
    val id: Int,
    val title: String,
    val image: String? = null,
    val imageType: String? = null,
    val servings: Int,
    val readyInMinutes: Int
)
