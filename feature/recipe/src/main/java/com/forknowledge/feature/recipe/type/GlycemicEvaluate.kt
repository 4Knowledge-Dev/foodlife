package com.forknowledge.feature.recipe.type

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.forknowledge.core.ui.theme.Green86BF3E
import com.forknowledge.core.ui.theme.GreenA1CE50
import com.forknowledge.core.ui.theme.OrangeFB880C
import com.forknowledge.core.ui.theme.RedFF4950
import com.forknowledge.core.ui.theme.YellowFFAE01
import com.forknowledge.core.ui.theme.YellowFFC04C
import com.forknowledge.feature.recipe.R

enum class HealthScoreEvaluate(
    @StringRes val evaluate: Int,
    @ColorRes val color: Color
) {
    EXCELLENT(R.string.recipe_health_score_evaluate_excellent, Green86BF3E),
    GOOD(R.string.recipe_health_score_evaluate_good, GreenA1CE50),
    FAIR(R.string.recipe_health_score_evaluate_fair, YellowFFC04C),
    POOR(R.string.recipe_health_score_evaluate_poor, OrangeFB880C),
    BAD(R.string.recipe_health_score_evaluate_bad, RedFF4950)
}

fun Int.toHealthScoreEvaluate() = when (this) {
    in 90..100 -> HealthScoreEvaluate.EXCELLENT
    in 75..89 -> HealthScoreEvaluate.GOOD
    in 60..74 -> HealthScoreEvaluate.FAIR
    in 40..59 -> HealthScoreEvaluate.POOR
    else -> HealthScoreEvaluate.BAD
}

enum class GlycemicEvaluate(
    @StringRes val evaluate: Int,
    @ColorRes val color: Color
) {
    HIGH(R.string.recipe_health_score_glycemic_evaluate_high, RedFF4950),
    MEDIUM(R.string.recipe_health_score_glycemic_evaluate_medium, YellowFFAE01),
    LOW(R.string.recipe_health_score_glycemic_evaluate_low, Green86BF3E)
}

fun Int.toGlycemicIndexEvaluate() = when (this) {
    in 0..55 -> GlycemicEvaluate.LOW
    in 56..69 -> GlycemicEvaluate.MEDIUM
    else -> GlycemicEvaluate.HIGH
}

fun Int.toGlycemicLoadEvaluate() = when (this) {
    in 0..10 -> GlycemicEvaluate.LOW
    in 11..19 -> GlycemicEvaluate.MEDIUM
    else -> GlycemicEvaluate.HIGH
}
