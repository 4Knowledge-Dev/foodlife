package com.forknowledge.core.api.model

import com.forknowledge.feature.model.userdata.Recipe
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.math.roundToLong

@InternalSerializationApi
@Serializable
data class SearchResponse(
    @SerialName("results")
    val recipes: List<RecipeResponse>,
    @SerialName("offset")
    val index: Int,
    @SerialName("number")
    val pageSize: Int,
)

@InternalSerializationApi
@Serializable
data class RecipeResponse(
    val id: Long,
    val title: String,
    val image: String,
    val servings: Int,
    val healthScore: Double,
    val nutrition: NutritionResponse
) {

    fun toRecipe() = Recipe(
        id = id,
        name = title,
        imageUrl = image,
        healthScore = healthScore.roundToLong(),
        nutrients = nutrition.nutrients.map { it.toNutrient() }
    )
}
