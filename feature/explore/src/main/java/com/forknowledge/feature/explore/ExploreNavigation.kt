package com.forknowledge.feature.explore

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable

@Serializable
data class ExploreRoute(
    val isAddMealPlanProcess: Boolean,
    val mealPosition: Int,
    val dateInMillis: Long,
)

@Serializable
data class ExploreSearchRoute(
    val isAddMealPlanProcess: Boolean,
    val mealPosition: Int,
    val dateInMillis: Long,
)

fun NavController.navigateToExplore(navOptions: NavOptions? = null) {
    navigate(ExploreRoute(false, 0, 0), navOptions)
}
