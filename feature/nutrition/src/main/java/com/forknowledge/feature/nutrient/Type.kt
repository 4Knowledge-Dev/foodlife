package com.forknowledge.feature.nutrient

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.forknowledge.core.common.AppConstant.RECIPE_NUTRIENT_CALORIES_INDEX
import com.forknowledge.core.ui.theme.OrangeFF9524

enum class Utils {
    SUCCESS, FAIL, NONE
}

enum class StatisticsTabRow(@StringRes val title: Int) {
    DAILY(R.string.statistics_daily_tab_label),
    WEEKLY(R.string.statistics_weekly_tab_label),
    MONTHLY(R.string.statistics_monthly_tab_label)
}

enum class StatisticsType(
    @StringRes val title: Int,
    val nutritionIndex: Int,
    @ColorRes val textColor: Color

) {
    DIETARY_ENERGY(R.string.statistics_dietary_energy, RECIPE_NUTRIENT_CALORIES_INDEX, OrangeFF9524),
    DIETARY_INTAKE(R.string.statistics_dietary_intake, 0, Color.Unspecified)
}