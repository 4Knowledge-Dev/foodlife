package com.forknowledge.feature.nutrient

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.forknowledge.core.ui.theme.Blue05A6F1
import com.forknowledge.core.ui.theme.Blue4C5BB5
import com.forknowledge.core.ui.theme.Blue6ABFFF
import com.forknowledge.core.ui.theme.Green70BF0C
import com.forknowledge.core.ui.theme.OrangeFF9524
import com.forknowledge.core.ui.theme.RedFF3939
import com.forknowledge.core.ui.theme.YellowFFAE01
import com.forknowledge.core.ui.theme.YellowFFC04C

enum class Utils {
    SUCCESS, FAIL, NONE
}

enum class StatisticsTabRow(@StringRes val title: Int) {
    DAILY(R.string.statistics_daily_tab_label),
    WEEKLY(R.string.statistics_weekly_tab_label),
    MONTHLY(R.string.statistics_monthly_tab_label)
}

enum class NutritionGroupType(
    @DrawableRes val icon: Int,
    @ColorRes val color: Color

) {
    ENERGY(
        R.drawable.img_energy,
        OrangeFF9524
    ),
    CARB(
        R.drawable.img_carbs,
        RedFF3939
    ),
    PROTEIN(
        R.drawable.img_protein,
        Blue05A6F1
    ),
    FAT(
        R.drawable.img_fat,
        YellowFFAE01
    ),
    OTHER_NUTRIENT(
        R.drawable.img_other_nutrition,
        Green70BF0C
    ),
    VITAMINS(
        R.drawable.img_snack,
        YellowFFC04C
    ),
    MINERALS(
        R.drawable.img_minerals,
        Blue4C5BB5
    ),
    ACTIVITY(
        R.drawable.img_lunch,
        Blue6ABFFF
    )
}
