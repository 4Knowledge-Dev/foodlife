package com.forknowledge.core.api.model

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
    val position: Int,
    val slot: Int,
    val type: String,
    val value: ValueResponse
)

@InternalSerializationApi
@Serializable
data class ValueResponse(
    val id: Int,
    val image: String,
    val servings: Int,
    val title: String
)
