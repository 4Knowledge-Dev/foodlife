package com.forknowledge.foodlife.ui

import androidx.annotation.StringRes
import com.forknowledge.feature.explore.ExploreRoute
import com.forknowledge.feature.nutrient.NutrientRoute
import com.forknowledge.feature.planner.PlannerRoute
import com.forknowledge.foodlife.R
import kotlin.reflect.KClass

enum class TopLevelDestination(
    val unselectedIcon: Int,
    val selectedIcon: Int,
    @StringRes val titleText: Int,
    val route: KClass<*>,
) {

    PLANNER(
        unselectedIcon = R.drawable.ic_unselected_planner,
        selectedIcon = R.drawable.ic_selected_planner,
        titleText = R.string.top_level_destination_planner_title,
        route = PlannerRoute::class
    ),

    NUTRIENT(
        unselectedIcon = R.drawable.ic_unselected_nutrient,
        selectedIcon = R.drawable.ic_selected_nutrient,
        titleText = R.string.top_level_destination_nutrient_title,
        route = NutrientRoute::class
    ),

    EXPLORE(
        unselectedIcon = R.drawable.ic_unselected_search,
        selectedIcon = R.drawable.ic_selected_search,
        titleText = R.string.top_level_destination_explore_title,
        route = ExploreRoute::class
    )
}
