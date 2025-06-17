package com.forknowledge.core.api.model

import com.forknowledge.feature.model.Nutrient
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@InternalSerializationApi
@Serializable
data class NutritionResponse(
    val nutrients: List<NutrientResponse>
)

@InternalSerializationApi
@Serializable
data class NutrientResponse(
    val name: String,
    val amount: Float,
    val unit: String,
) {

    fun toNutrient() = Nutrient(
        name = name,
        amount = amount,
        unit = unit
    )
}
