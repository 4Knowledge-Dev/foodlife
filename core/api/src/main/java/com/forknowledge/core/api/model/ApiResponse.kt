package com.forknowledge.core.api.model

import com.forknowledge.feature.model.Recipe
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@InternalSerializationApi
@Serializable
data class SearchResponse(
    @SerialName("results")
    val recipes: List<RecipeResponse>,
    @SerialName(value = "offset")
    val index: Int,
    @SerialName(value = "number")
    val pageSize: Int,
)

@InternalSerializationApi
@Serializable
data class RecipeResponse(
    val id: String,
    val title: String,
    val image: String,
) {

    fun toRecipe() = Recipe(
        id = id,
        title = title,
        image = image,
    )
}