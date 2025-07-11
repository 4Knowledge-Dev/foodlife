package com.forknowledge.core.api.model

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@InternalSerializationApi
@Serializable
data class NutritionResponse(
    val nutrients: List<NutrientResponse>
)
