package com.forknowledge.feature.recipe.type

import androidx.annotation.StringRes
import com.forknowledge.feature.recipe.R

enum class RecipeTab(@StringRes val title: Int) {
    INGREDIENTS(R.string.recipe_ingredients_tab_title),
    INSTRUCTIONS(R.string.recipe_instructions_tab_title),
    HEALTH(R.string.recipe_health_tab_title)
}