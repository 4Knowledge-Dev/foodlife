package com.forknowledge.core.api.model

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@InternalSerializationApi
@Serializable
data class GenerateMealPlanResponse(
    val week: WeekPlanResponse
)

@InternalSerializationApi
@Serializable
data class WeekPlanResponse(
    val sunday: DayPlanResponse,
    val monday: DayPlanResponse,
    val tuesday: DayPlanResponse,
    val wednesday: DayPlanResponse,
    val thursday: DayPlanResponse,
    val friday: DayPlanResponse,
    val saturday: DayPlanResponse,
){
    fun toDayPlanResponseList() = listOf(
        sunday,
        monday,
        tuesday,
        wednesday,
        thursday,
        friday,
        saturday
    )
}

@InternalSerializationApi
@Serializable
data class DayPlanResponse(
    val meals: List<MealResponse>
)

@InternalSerializationApi
@Serializable
data class MealResponse(
    val id: Int,
    val title: String,
    val image: String,
    val imageType: String,
    val readyInMinutes: Int,
    val servings: Int
)