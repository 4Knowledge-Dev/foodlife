package com.forknowledge.foodlife.ui

import com.forknowledge.foodlife.R

enum class TopLevelDestination(
    val unselectedIcon: Int,
    val selectedIcon: Int,
    val titleText: Int
) {

    PLANNER(
        unselectedIcon = R.drawable.ic_planner,
        selectedIcon = R.drawable.ic_planner,
        titleText = R.string.top_level_destination_planner_title
    ),

    WORKOUT(
        unselectedIcon = R.drawable.ic_workout_outline,
        selectedIcon = R.drawable.ic_workout_filled,
        titleText = R.string.top_level_destination_workout_title
    ),

    TRACK(
        unselectedIcon = R.drawable.ic_calories_track,
        selectedIcon = R.drawable.ic_calories_track,
        titleText = R.string.top_level_destination_track_title
    ),

    PROFILE(
        unselectedIcon = R.drawable.ic_profile_outline,
        selectedIcon = R.drawable.ic_profile_filled,
        titleText = R.string.top_level_destination_profile_title
    )
}
