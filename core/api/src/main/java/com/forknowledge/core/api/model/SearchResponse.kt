package com.forknowledge.core.api.model

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

    fun toSearchRecipe(): SearchRecipe {
        val responseNutrients = nutrition?.nutrients ?: emptyList()
        val nutrients = mutableListOf<NutrientResponse>()
        nutrients.add(responseNutrients.firstOrNull {
            it.name == NUTRIENT_TYPE_NAME_CALORIES
        }!!)
        nutrients.add(responseNutrients.firstOrNull {
            it.name == NUTRIENT_TYPE_NAME_CARBOHYDRATES
        }!!)
        nutrients.add(responseNutrients.firstOrNull {
            it.name == NUTRIENT_TYPE_NAME_PROTEIN
        }!!)
        nutrients.add(responseNutrients.firstOrNull {
            it.name == NUTRIENT_TYPE_NAME_FAT
        }!!)

        return SearchRecipe(
            id = id,
            name = title,
            imageUrl = image,
            servings = servings,
            cookTime = readyInMinutes ?: 0,
            nutrients = nutrients.map { it.toNutrient() }
        )
    }
}
