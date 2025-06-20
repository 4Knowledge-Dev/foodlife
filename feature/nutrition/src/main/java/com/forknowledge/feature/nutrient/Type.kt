package com.forknowledge.feature.nutrient

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.forknowledge.core.common.AppConstant.RECIPE_NUTRIENT_CALORIES_INDEX
import com.forknowledge.core.common.AppConstant.RECIPE_NUTRIENT_CARB_INDEX
import com.forknowledge.core.common.AppConstant.RECIPE_NUTRIENT_FAT_INDEX
import com.forknowledge.core.common.AppConstant.RECIPE_NUTRIENT_PROTEIN_INDEX
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
    @DrawableRes val icon: Int,
    @ColorRes val color: Color

) {
    ENERGY(
        R.string.statistics_dietary_energy,
        RECIPE_NUTRIENT_CALORIES_INDEX,
        R.drawable.img_vector_energy,
        OrangeFF9524
    ),
    CARB(
        R.string.statistics_dietary_intake,
        RECIPE_NUTRIENT_CARB_INDEX,
        R.drawable.img_vector_carbs,
        Color.Unspecified
    ),
    PROTEIN(
        R.string.statistics_dietary_intake,
        RECIPE_NUTRIENT_PROTEIN_INDEX,
        R.drawable.img_vector_protein,
        Color.Unspecified
    ),
    FAT(
        R.string.statistics_dietary_intake,
        RECIPE_NUTRIENT_FAT_INDEX,
        R.drawable.img_vector_fat,
        Color.Unspecified
    )
}
