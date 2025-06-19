package com.forknowledge.feature.nutrient

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable

@Serializable
object NutrientRoute

@Serializable
data class SearchRoute(
    val mealPosition: Int,
    val dateInMillis: Long
)

@Serializable
data class InsightsRoute(
    val dateInMillis: Long
)

fun NavController.navigateToNutrient(navOptions: NavOptions? = null) {
    navigate(NutrientRoute, navOptions)
}
