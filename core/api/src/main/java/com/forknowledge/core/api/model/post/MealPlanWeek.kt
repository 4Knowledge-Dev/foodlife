package com.forknowledge.core.api.model.post

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@InternalSerializationApi
@Serializable
data class MealItem(
    val date: Long,
    val slot: Int,
    val position: Int,
    val type: String,
    @SerialName("value")
    val recipe: MealRecipeItem
)

@InternalSerializationApi
@Serializable
data class MealRecipeItem(
    val id: Int,
    val title: String,
    val image: String,
    val servings: Int,
    val readyInMinutes: Int,
    val calories: Int? = null,
    val carbs: Int? = null,
    val protein: Int? = null,
    val fat: Int? = null
)
