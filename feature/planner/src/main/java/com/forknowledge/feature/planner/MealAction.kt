package com.forknowledge.feature.planner

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

enum class MealAction(
    @StringRes val label: Int,
    @DrawableRes val icon: Int
) {
    COMPLETE(R.string.meal_planner_bottom_sheet_add_recipe_label, R.drawable.ic_complete),
    SWAP(R.string.meal_planner_bottom_sheet_swap_recipe_label, R.drawable.ic_swap),
    DELETE(R.string.meal_planner_bottom_sheet_delete_recipe_label, R.drawable.ic_delete)
}
