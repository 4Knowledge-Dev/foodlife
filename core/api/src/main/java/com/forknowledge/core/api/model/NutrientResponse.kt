package com.forknowledge.core.api.model

import com.forknowledge.feature.model.Nutrient
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable
import kotlin.math.roundToInt

@InternalSerializationApi
@Serializable
data class NutrientResponse(
    val name: String,
    val amount: Float,
    val unit: String,
    val percentOfDailyNeeds: Float
) {
    fun toNutrient() = Nutrient(
        name = name,
        amount = amount,
        unit = unit,
        dailyPercentValue = percentOfDailyNeeds.roundToInt()
    )
}