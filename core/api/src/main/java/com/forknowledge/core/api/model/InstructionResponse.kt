package com.forknowledge.core.api.model

import com.forknowledge.core.api.getImageUrl
import com.forknowledge.feature.model.Equipment
import com.forknowledge.feature.model.Ingredient
import com.forknowledge.feature.model.Step
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@InternalSerializationApi
@Serializable
data class InstructionResponse(
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
