package com.forknowledge.feature.nutrient

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable

@Serializable object NutrientRoute

fun NavController.navigateToNutrient(navOptions: NavOptions? = null) {
    navigate(NutrientRoute, navOptions)
}
