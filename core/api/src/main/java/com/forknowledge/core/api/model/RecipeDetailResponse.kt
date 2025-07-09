package com.forknowledge.core.api.model

import com.forknowledge.core.api.getImageUrl
import com.forknowledge.feature.model.Ingredient
import com.forknowledge.feature.model.Measure
import com.forknowledge.feature.model.Property
import com.forknowledge.feature.model.Recipe
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.math.roundToInt

const val TYPE_INGREDIENTS = "INGREDIENTS"
const val TYPE_EQUIPMENTS = "EQUIPMENT"

@InternalSerializationApi
@Serializable
data class RecipeDetailResponse(
    val id: Int,
    val image: String,
    val title: String,
    val summary: String?,
    val readyInMinutes: Int?,
    val preparationMinutes: Int?,
    val cookingMinutes: Int?,
    val servings: Int,
    val sourceName: String,
    val sourceUrl: String?,
    val healthScore: Float,
    val nutrition: RecipeNutritionResponse,
    @SerialName("extendedIngredients")
    val ingredients: List<ExtendedIngredient>,
    @SerialName("analyzedInstructions")
    val instruction: List<InstructionResponse>
) {
    fun toRecipe() = Recipe(
        recipeId = id,
        imageUrl = image,
        recipeName = title,
        summary = summary ?: "",
        readyInMinutes = readyInMinutes ?: 0,
        preparationMinutes = preparationMinutes ?: 0,
        cookingMinutes = cookingMinutes ?: 0,
        servings = servings,
        sourceName = sourceName,
        sourceUrl = sourceUrl ?: "",
        healthScore = healthScore.roundToInt(),
        nutrition = nutrition.nutrients.map { it.toNutrient() },
        properties = nutrition.properties.map { it.toProperty() },
        ingredients = ingredients.map { it.toIngredient() },
        steps = instruction.firstOrNull()?.steps?.map { it.toInstruction() } ?: emptyList()
    )
}

@InternalSerializationApi
@Serializable
data class ExtendedIngredient(
    val id: Int,
    val name: String,
    val image: String,
    val amount: Float,
    val unit: String,
    val measures: MeasuresResponse
) {
    fun toIngredient() = Ingredient(
        ingredientId = id,
        ingredientName = name,
        imageUrl = getImageUrl(
            image = image,
            mealType = TYPE_INGREDIENTS
        ),
        originalMeasures = Measure(
            amount = amount,
            unit = unit
        ),
        metricMeasures = measures.metric.toMetricMeasure(),
        usMeasures = measures.us.toUsMeasure()
    )
}

@InternalSerializationApi
@Serializable
data class MeasuresResponse(
    val metric: Metric,
    val us: Us
)

@InternalSerializationApi
@Serializable
data class Metric(
    val amount: Float,
    val unitLong: String,
    val unitShort: String
) {
    fun toMetricMeasure() = Measure(
        amount = amount,
        unit = unitShort
    )
}

@InternalSerializationApi
@Serializable
data class Us(
    val amount: Float,
    val unitLong: String,
    val unitShort: String
) {
    fun toUsMeasure() = Measure(
        amount = amount,
        unit = unitShort
    )
}

@InternalSerializationApi
@Serializable
data class RecipeNutritionResponse(
    val nutrients: List<NutrientResponse>,
    val properties: List<PropertiesResponse>
)

@InternalSerializationApi
@Serializable
data class PropertiesResponse(
    val name: String,
    val amount: Float,
) {
    fun toProperty() = Property(
        name = name,
        value = amount.roundToInt()
    )
}
