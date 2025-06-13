package com.forknowledge.core.api.model

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@InternalSerializationApi
@Serializable
data class GenerateMealPlanResponse(
    val week: WeekResponse
)

@InternalSerializationApi
@Serializable
data class WeekResponse(
    val friday: MealDayResponse,
    val monday: MealDayResponse,
    val saturday: MealDayResponse,
    val sunday: MealDayResponse,
    val thursday: MealDayResponse,
    val tuesday: MealDayResponse,
    val wednesday: MealDayResponse
)

@InternalSerializationApi
@Serializable
data class MealDayResponse(
    val meals: List<MealResponse>
)

@InternalSerializationApi
@Serializable
data class MealResponse(
    val id: Int,
    val image: String,
    val imageType: String,
    val readyInMinutes: Int,
    val servings: Int,
    val sourceUrl: String,
    val title: String
)