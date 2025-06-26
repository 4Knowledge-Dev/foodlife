package com.forknowledge.core.api.model

import com.forknowledge.feature.model.Nutrient
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable
import kotlin.math.roundToInt

@InternalSerializationApi
@Serializable
data class NutritionResponse(
    val nutrients: List<NutrientResponse>
)
