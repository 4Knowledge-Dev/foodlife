package com.forknowledge.feature.recipe

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.forknowledge.core.ui.theme.Green86BF3E
import com.forknowledge.core.ui.theme.RedFF4950
import com.forknowledge.core.ui.theme.YellowFFAE01

enum class HealthScoreEvaluate(
    @StringRes val evaluate: Int,
    @ColorRes val color: Color
) {
    HIGH(R.string.recipe_health_score_glycemic_evaluate_high, RedFF4950),
    MEDIUM(R.string.recipe_health_score_glycemic_evaluate_medium, YellowFFAE01),
    LOW(R.string.recipe_health_score_glycemic_evaluate_low, Green86BF3E)
}