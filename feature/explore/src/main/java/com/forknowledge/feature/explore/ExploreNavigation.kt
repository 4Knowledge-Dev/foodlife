package com.forknowledge.feature.explore

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable

@Serializable data object ExploreRoute

fun NavController.navigateToExplore(navOptions: NavOptions? = null) {
    navigate(ExploreRoute, navOptions)
}
