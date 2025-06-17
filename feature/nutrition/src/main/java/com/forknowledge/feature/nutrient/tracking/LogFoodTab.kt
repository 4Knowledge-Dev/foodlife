package com.forknowledge.feature.nutrient.tracking

import androidx.annotation.StringRes
import com.forknowledge.feature.nutrient.R

enum class LogFoodTab(@StringRes val label: Int) {
    FREQUENT(R.string.nutrient_log_food_tab_label_frequent),
    PLAN(R.string.nutrient_log_food_tab_label_plan),
    SAVED(R.string.nutrient_log_food_tab_label_saved)
}
