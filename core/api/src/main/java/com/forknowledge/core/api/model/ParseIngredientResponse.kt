package com.forknowledge.core.api.model


import com.forknowledge.core.api.getImageUrl
import com.forknowledge.feature.model.Ingredient
import com.forknowledge.feature.model.Measure
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

typealias ParseIngredientResponse = List<ParseIngredientResponseItem>

@Serializable
data class ParseIngredientResponseItem(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("name")
    val name: String,
    @SerialName("unit")
    val unit: String? = null,
    @SerialName("amount")
    val amount: Float,
    @SerialName("image")
    val image: String? = null
) {
    fun toIngredient() = Ingredient(
        ingredientId = id ?: 0,
        ingredientName = name,
        imageUrl = image?.let {
            getImageUrl(
                image = image,
                mealType = TYPE_INGREDIENTS
            )
        } ?: "",
        originalMeasures = Measure(
            amount = amount,
            unit = unit ?: ""
        )
    )
}