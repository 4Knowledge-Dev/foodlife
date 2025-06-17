package com.forknowledge.core.api.model

import com.forknowledge.feature.model.Nutrient
import com.forknowledge.feature.model.SearchRecipe
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

const val NUTRIENT_TYPE_NAME_CALORIES = "Calories"
const val NUTRIENT_TYPE_NAME_CARBOHYDRATES = "Carbohydrates"
const val NUTRIENT_TYPE_NAME_PROTEIN = "Protein"
const val NUTRIENT_TYPE_NAME_FAT = "Fat"

@InternalSerializationApi
@Serializable
data class SearchResponse(
    @SerialName("results")
    val recipes: List<RecipeResponse>
)

@InternalSerializationApi
@Serializable
data class RecipeResponse(
    val id: Int,
    val title: String,
    val image: String,
    val imageType: String,
    val servings: Int,
    val readyInMinutes: Int? = null,
    val nutrition: NutritionResponse? = null
) {

    fun toSearchRecipe() = SearchRecipe(
        id = id,
        name = title,
        imageUrl = image,
        servings = servings,
        cookTime = readyInMinutes ?: 0,
        nutrients = nutrition?.nutrients?.takeWhile {
            it.name == NUTRIENT_TYPE_NAME_CALORIES
                    || it.name == NUTRIENT_TYPE_NAME_CARBOHYDRATES
                    || it.name == NUTRIENT_TYPE_NAME_PROTEIN
                    || it.name == NUTRIENT_TYPE_NAME_FAT
        }?.map { it.toNutrient() } ?: emptyList<Nutrient>()
    )
}
