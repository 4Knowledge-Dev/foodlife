package com.forknowledge.feature.planner

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable

@Serializable
data object PlannerRoute

fun NavController.navigateToPlanner(navOptions: NavOptions? = null) {
    navigate(PlannerRoute, navOptions)
}

fun NavController.navigateToPlannerRoute() {
    navigate(PlannerRoute) {
        popUpTo(graph.findStartDestination().id) {
            inclusive = true
        }
    }
}
