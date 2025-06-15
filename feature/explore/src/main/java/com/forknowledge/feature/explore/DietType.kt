package com.forknowledge.feature.explore

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

enum class DietType(
    @StringRes val title: Int,
    @DrawableRes val icon: Int
) {
    HIGH_PROTEIN(R.string.explore_diet_type_high_protein, R.drawable.img_diet_high_protein),
    LOW_CARB(R.string.explore_diet_type_low_carb, R.drawable.img_diet_low_carb),
    GLUTEN_FREE(R.string.explore_diet_type_gluten_free, R.drawable.img_diet_gluten_free),
    VEGAN(R.string.explore_diet_type_vegan, R.drawable.img_diet_vegan),
    KETO(R.string.explore_diet_type_keto, R.drawable.img_diet_keto),
    PESCETARIAN(R.string.explore_diet_type_pescetarian, R.drawable.img_diet_pescetarian)
}