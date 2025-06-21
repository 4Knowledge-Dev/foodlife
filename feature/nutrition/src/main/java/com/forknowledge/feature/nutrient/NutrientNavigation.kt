package com.forknowledge.feature.nutrient

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable

@Serializable
data object NutrientRoute

@Serializable
data class SearchRoute(
    val mealPosition: Int,
    val dateInMillis: Long
)

@Serializable
data class InsightsRoute(
    val dateInMillis: Long
)

@Serializable
data object NutrientGroupRoute

@Serializable
data class AdditionalNutritionRoute(
    val groupName: String,
    val nutritionType: NutritionGroupType
)

@Serializable
data class StatisticsRoute(
    val nutrientName: String,
    val type: NutritionGroupType
)

fun NavController.navigateToNutrient(navOptions: NavOptions? = null) {
    navigate(NutrientRoute, navOptions)
}
