package com.forknowledge.core.api.model

import com.forknowledge.feature.model.userdata.Nutrient
import com.forknowledge.feature.model.SearchRecipe
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.math.roundToInt

@InternalSerializationApi
@Serializable
data class SearchResponse(
    @SerialName("results")
    val recipes: List<RecipeResponse>
)

@InternalSerializationApi
@Serializable
data class RecipeResponse(
    val id: Long,
    val title: String,
    val image: String,
    val imageType: String,
    val servings: Int,
    val healthScore: Double,
    val readyInMinutes: Int? = null,
    val nutrition: NutritionResponse? = null
) {

    fun toRecipe() = SearchRecipe(
        id = id,
        name = title,
        imageUrl = image,
        healthScore = healthScore.roundToInt(),
        servings = servings,
        cookTime = readyInMinutes ?: 0,
        nutrients = nutrition?.nutrients?.map { it.toNutrient() } ?: emptyList<Nutrient>()
    )
}
