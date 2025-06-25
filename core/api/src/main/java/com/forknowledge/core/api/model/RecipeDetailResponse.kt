package com.forknowledge.core.api.model

import com.forknowledge.core.api.getImageUrl
import com.forknowledge.feature.model.Equipment
import com.forknowledge.feature.model.Ingredient
import com.forknowledge.feature.model.Step
import com.forknowledge.feature.model.Measure
import com.forknowledge.feature.model.Recipe
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

const val TYPE_INGREDIENTS = "INGREDIENTS"
const val TYPE_EQUIPMENTS = "EQUIPMENT"

@InternalSerializationApi
@Serializable
data class RecipeDetailResponse(
    val id: Int,
    val image: String,
    val title: String,
    val summary: String,
    val readyInMinutes: Int?,
    val preparationMinutes: Int?,
    val cookingMinutes: Int?,
    val servings: Int,
    val sourceName: String,
    val sourceUrl: String,
    val healthScore: Float,
    @SerialName("extendedIngredients")
    val ingredients: List<ExtendedIngredient>,
    @SerialName("analyzedInstructions")
    val instruction: List<AnalyzedInstruction>
) {
    fun toRecipe() = Recipe(
        recipeId = id,
        imageUrl = image,
        recipeName = title,
        summary = summary,
        readyInMinutes = readyInMinutes ?: 0,
        preparationMinutes = preparationMinutes ?: 0,
        cookingMinutes = cookingMinutes ?: 0,
        servings = servings,
        sourceName = sourceName,
        sourceUrl = sourceUrl,
        healthScore = healthScore,
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
data class AnalyzedInstruction(
    val steps: List<StepResponse>
)

@InternalSerializationApi
@Serializable
data class StepResponse(
    val number: Int,
    val step: String,
    val equipment: List<EquipmentResponse>,
    val ingredients: List<IngredientResponse>
) {
    fun toInstruction() = Step(
        stepNumber = number,
        description = step,
        equipments = equipment.map { it.toEquipment() },
        ingredients = ingredients.map { it.toIngredient() }
    )
}

@InternalSerializationApi
@Serializable
data class EquipmentResponse(
    val id: Int,
    val image: String,
    val name: String
) {
    fun toEquipment() = Equipment(
        equipmentId = id,
        equipmentName = name,
        imageUrl = getImageUrl(
            image = image,
            mealType = TYPE_EQUIPMENTS
        )
    )
}

@InternalSerializationApi
@Serializable
data class IngredientResponse(
    val id: Int,
    val image: String,
    val name: String
) {
    fun toIngredient() = Ingredient(
        ingredientId = id,
        ingredientName = name,
        imageUrl = getImageUrl(
            image = image,
            mealType = TYPE_INGREDIENTS
        )
    )
}
