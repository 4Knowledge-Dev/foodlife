package com.forknowledge.core.api.model

import com.forknowledge.feature.model.SearchRecipe
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

const val NUTRIENT_TYPE_NAME_CALORIES = "Calories"
const val NUTRIENT_TYPE_NAME_CARBOHYDRATES = "Carbohydrates"
const val NUTRIENT_TYPE_NAME_NET_CARBOHYDRATES = "Net Carbohydrates"
const val NUTRIENT_TYPE_NAME_FIBER = "Fiber"
const val NUTRIENT_TYPE_NAME_SUGAR = "Sugar"
const val NUTRIENT_TYPE_NAME_PROTEIN = "Protein"
const val NUTRIENT_TYPE_NAME_FAT = "Fat"
const val NUTRIENT_TYPE_NAME_SATURATED_FAT = "Saturated Fat"
const val NUTRIENT_UNIT_CALORIES = "kcal"
const val NUTRIENT_UNIT_GRAM = "g"

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
    val readyInMinutes: Int,
    val nutrition: NutritionResponse? = null
) {

    fun toSearchRecipe(): SearchRecipe {
        val responseNutrients = nutrition?.nutrients ?: emptyList()
        val nutrients = mutableListOf<NutrientResponse>()

        nutrients.addAll(
            listOf(
                responseNutrients.firstOrNull {
                    it.name == NUTRIENT_TYPE_NAME_CALORIES
                } ?: NutrientResponse(
                    name = NUTRIENT_TYPE_NAME_CALORIES,
                    amount = 0f,
                    unit = NUTRIENT_UNIT_CALORIES,
                    percentOfDailyNeeds = 0f
                ),
                responseNutrients.firstOrNull {
                    it.name == NUTRIENT_TYPE_NAME_CARBOHYDRATES
                } ?: NutrientResponse(
                    name = NUTRIENT_TYPE_NAME_CARBOHYDRATES,
                    amount = 0f,
                    unit = NUTRIENT_UNIT_GRAM,
                    percentOfDailyNeeds = 0f
                ),
                responseNutrients.firstOrNull {
                    it.name == NUTRIENT_TYPE_NAME_NET_CARBOHYDRATES
                } ?: NutrientResponse(
                    name = NUTRIENT_TYPE_NAME_NET_CARBOHYDRATES,
                    amount = 0f,
                    unit = NUTRIENT_UNIT_GRAM,
                    percentOfDailyNeeds = 0f
                ),
                responseNutrients.firstOrNull {
                    it.name == NUTRIENT_TYPE_NAME_FIBER
                } ?: NutrientResponse(
                    name = NUTRIENT_TYPE_NAME_FIBER,
                    amount = 0f,
                    unit = NUTRIENT_UNIT_GRAM,
                    percentOfDailyNeeds = 0f
                ),
                responseNutrients.firstOrNull {
                    it.name == NUTRIENT_TYPE_NAME_SUGAR
                } ?: NutrientResponse(
                    name = NUTRIENT_TYPE_NAME_SUGAR,
                    amount = 0f,
                    unit = NUTRIENT_UNIT_GRAM,
                    percentOfDailyNeeds = 0f
                ),
                responseNutrients.firstOrNull {
                    it.name == NUTRIENT_TYPE_NAME_PROTEIN
                } ?: NutrientResponse(
                    name = NUTRIENT_TYPE_NAME_PROTEIN,
                    amount = 0f,
                    unit = NUTRIENT_UNIT_GRAM,
                    percentOfDailyNeeds = 0f
                ),
                responseNutrients.firstOrNull {
                    it.name == NUTRIENT_TYPE_NAME_FAT
                } ?: NutrientResponse(
                    name = NUTRIENT_TYPE_NAME_FAT,
                    amount = 0f,
                    unit = NUTRIENT_UNIT_GRAM,
                    percentOfDailyNeeds = 0f
                ),
                responseNutrients.firstOrNull {
                    it.name == NUTRIENT_TYPE_NAME_SATURATED_FAT
                } ?: NutrientResponse(
                    name = NUTRIENT_TYPE_NAME_SATURATED_FAT,
                    amount = 0f,
                    unit = NUTRIENT_UNIT_GRAM,
                    percentOfDailyNeeds = 0f
                ),
            )
        )
        nutrients.addAll(
            responseNutrients.filter {
                it.name != NUTRIENT_TYPE_NAME_CALORIES &&
                        it.name != NUTRIENT_TYPE_NAME_PROTEIN &&
                        it.name != NUTRIENT_TYPE_NAME_CARBOHYDRATES &&
                        it.name != NUTRIENT_TYPE_NAME_NET_CARBOHYDRATES &&
                        it.name != NUTRIENT_TYPE_NAME_FAT &&
                        it.name != NUTRIENT_TYPE_NAME_SUGAR &&
                        it.name != NUTRIENT_TYPE_NAME_FIBER &&
                        it.name != NUTRIENT_TYPE_NAME_SATURATED_FAT
            }
        )

        return SearchRecipe(
            id = id,
            name = title,
            imageUrl = image,
            servings = servings,
            cookTime = readyInMinutes,
            nutrients = nutrients.map { it.toNutrient() }
        )
    }
}
