package com.forknowledge.core.api.model

import com.forknowledge.feature.model.userdata.Nutrient
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
    val amount: Double,
    val unit: String,
) {

    fun toNutrient() = Nutrient(
        name = name,
        amount = amount,
        unit = unit
    )
}
