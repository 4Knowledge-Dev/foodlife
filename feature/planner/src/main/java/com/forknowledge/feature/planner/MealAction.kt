package com.forknowledge.feature.planner

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.forknowledge.core.ui.R.drawable

enum class MealAction(
    @StringRes val label: Int,
    @DrawableRes val icon: Int
) {
    DELETE(R.string.meal_planner_bottom_sheet_delete_recipe_label, drawable.ic_delete)
}
