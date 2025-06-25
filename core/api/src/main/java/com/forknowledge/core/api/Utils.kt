package com.forknowledge.core.api

import com.forknowledge.core.api.datatype.MealType
import com.forknowledge.core.api.model.TYPE_INGREDIENTS

const val INGREDIENT_IMAGE_SIZE = "500x500"
const val MEAL_IMAGE_SIZE = "636x393"
const val MEAL_TYPE_INGREDIENT = "ingredients"
const val MEAL_TYPE_EQUIPMENT = "equipment"
const val MEAL_TYPE_RECIPE = "recipes"
const val MEAL_TYPE_PRODUCT = "products"
const val MEAL_TYPE_MENU_ITEM = "menu-items"

fun String.toMealType() = when (this) {
    "RECIPE" -> MealType.RECIPE
    "PRODUCT" -> MealType.PRODUCT
    "MENU_ITEM" -> MealType.MENU_ITEM
    "INGREDIENTS" -> MealType.INGREDIENTS
    "EQUIPMENT" -> MealType.EQUIPMENT
    else -> MealType.CUSTOM_FOOD
}

fun getImageUrl(
    id: String = "",
    image: String,
    mealType: String,
    imageType: String = ""
): String {
    if (image.isNotEmpty() && mealType != TYPE_INGREDIENTS) {
        return image
    }
    return when (mealType.toMealType()) {
        MealType.INGREDIENTS -> "${BuildConfig.API_IMAGE_URL}/${MEAL_TYPE_INGREDIENT}_$INGREDIENT_IMAGE_SIZE/$image"
        MealType.EQUIPMENT -> "${BuildConfig.API_IMAGE_URL}/${MEAL_TYPE_EQUIPMENT}_$INGREDIENT_IMAGE_SIZE/$image"
        MealType.RECIPE -> "${BuildConfig.API_IMAGE_URL}/$MEAL_TYPE_RECIPE/$id-$MEAL_IMAGE_SIZE.$imageType"
        MealType.PRODUCT -> "${BuildConfig.API_IMAGE_URL}/$MEAL_TYPE_PRODUCT/$id-$MEAL_IMAGE_SIZE.$imageType"
        MealType.MENU_ITEM -> "${BuildConfig.API_IMAGE_URL}/$MEAL_TYPE_MENU_ITEM/$id-$MEAL_IMAGE_SIZE.$imageType"
        MealType.CUSTOM_FOOD -> image
    }
}
