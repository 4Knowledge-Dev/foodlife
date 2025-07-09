package com.forknowledge.feature.recipe.screen.recipe

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.forknowledge.core.ui.R.drawable
import com.forknowledge.core.ui.theme.Black063336
import com.forknowledge.core.ui.theme.Grey8A949F
import com.forknowledge.core.ui.theme.GreyDADADA
import com.forknowledge.core.ui.theme.GreyF4F5F5
import com.forknowledge.core.ui.theme.GreyFAFAFA
import com.forknowledge.core.ui.theme.Typography
import com.forknowledge.core.ui.theme.component.AppText
import com.forknowledge.feature.model.Nutrient
import com.forknowledge.feature.model.Property
import com.forknowledge.feature.recipe.R
import com.forknowledge.feature.recipe.type.GlycemicEvaluate
import com.forknowledge.feature.recipe.type.toGlycemicIndexEvaluate
import com.forknowledge.feature.recipe.type.toGlycemicLoadEvaluate
import com.forknowledge.feature.recipe.type.toHealthScoreEvaluate

@Composable
fun HealthTabContent(
    healthScore: Int,
    nutrition: List<Nutrient>,
    properties: List<Property>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(
                horizontal = 16.dp,
                vertical = 24.dp
            )
    ) {
        HealthSectionHeader(
            label = stringResource(R.string.recipe_health_score_label),
            onShowInfoClick = {}
        )

        HealthScoreCard(
            modifier = Modifier.padding(top = 16.dp),
            healthScore = healthScore,
            glycemicIndex = properties.getOrNull(0)?.value ?: 0,
            glycemicLoad = properties.getOrNull(1)?.value ?: 0
        )

        HealthSectionHeader(
            modifier = Modifier.padding(top = 20.dp),
            label = stringResource(R.string.recipe_health_score_nutrition_label),
            onShowInfoClick = {}
        )

        NutritionTable(
            modifier = Modifier.padding(top = 16.dp),
            nutrition = nutrition
        )

        AppText(
            modifier = Modifier.padding(top = 8.dp),
            text = stringResource(R.string.recipe_health_score_nutrition_table_note),
            textStyle = Typography.bodySmall,
            color = Grey8A949F
        )
    }
}

@Composable
fun HealthSectionHeader(
    modifier: Modifier = Modifier,
    label: String,
    onShowInfoClick: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        AppText(
            text = label,
            textStyle = Typography.titleSmall
        )

        IconButton(
            onClick = { onShowInfoClick() }
        ) {
            Icon(
                painter = painterResource(drawable.ic_tooltips),
                tint = Grey8A949F,
                contentDescription = null
            )
        }
    }
}

@Composable
fun HealthScoreCard(
    modifier: Modifier = Modifier,
    healthScore: Int,
    glycemicIndex: Int,
    glycemicLoad: Int,
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                shape = RoundedCornerShape(12.dp)
                shadowElevation = 3.dp.toPx()
                spotShadowColor = Black063336
                clip = true
            }
            .background(
                color = GreyFAFAFA,
                shape = RoundedCornerShape(12.dp)
            )
            .clip(RoundedCornerShape(12.dp))
            .padding(
                top = 16.dp,
                bottom = 16.dp,
                end = 24.dp
            )
    ) {
        val (scoreLabel, score, divider, glycemicIndexRow, glycemicLoadRow) = createRefs()
        val guideline = createGuidelineFromStart(0.3f)

        val healthScoreEvaluate = healthScore.toHealthScoreEvaluate()
        val annotatedScore = buildAnnotatedString {
            withStyle(SpanStyle(color = healthScoreEvaluate.color)) { append(healthScore.toString()) }
            withStyle(SpanStyle(color = Grey8A949F)) {
                append(stringResource(R.string.recipe_health_score_max_point))
            }
        }

        AppText(
            modifier = Modifier.constrainAs(scoreLabel) {
                top.linkTo(parent.top)
                bottom.linkTo(score.top)
                start.linkTo(parent.start)
                end.linkTo(guideline)
            },
            text = stringResource(R.string.recipe_health_score_core_label),
            textStyle = Typography.labelMedium,
            color = Grey8A949F
        )

        Text(
            modifier = Modifier.constrainAs(score) {
                top.linkTo(scoreLabel.bottom)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(guideline)
            },
            text = annotatedScore,
            style = Typography.displaySmall
        )

        VerticalDivider(
            modifier = Modifier
                .constrainAs(divider) {
                    top.linkTo(glycemicIndexRow.top)
                    bottom.linkTo(glycemicLoadRow.bottom)
                    end.linkTo(guideline)
                    height = Dimension.fillToConstraints
                },
            thickness = 1.dp,
            color = Grey8A949F
        )

        GlycemicSection(
            modifier = Modifier
                .constrainAs(glycemicIndexRow) {
                    top.linkTo(parent.top)
                    bottom.linkTo(glycemicLoadRow.top)
                    start.linkTo(divider.end, margin = 24.dp)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                },
            label = stringResource(R.string.recipe_health_score_glycemic_index),
            value = glycemicIndex,
            evaluate = glycemicIndex.toGlycemicIndexEvaluate()
        )

        GlycemicSection(
            modifier = Modifier
                .constrainAs(glycemicLoadRow) {
                    top.linkTo(glycemicIndexRow.bottom, margin = 8.dp)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(divider.end, margin = 24.dp)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                },
            label = stringResource(R.string.recipe_health_score_glycemic_load),
            value = glycemicLoad,
            evaluate = glycemicLoad.toGlycemicLoadEvaluate()
        )
    }
}

@Composable
fun GlycemicSection(
    modifier: Modifier = Modifier,
    label: String,
    value: Int,
    evaluate: GlycemicEvaluate
) {
    ConstraintLayout(modifier = modifier) {
        val (glycemicLabel, glycemicValue, glycemicEvaluate) = createRefs()
        val guideline = createGuidelineFromEnd(0.4f)

        AppText(
            modifier = Modifier.constrainAs(glycemicLabel) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(glycemicValue.start)
                horizontalBias = 0f
                width = Dimension.fillToConstraints
            },
            text = label,
            textStyle = Typography.bodyMedium
        )

        Box(
            modifier = Modifier
                .width(40.dp)
                .background(
                    color = evaluate.color,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(vertical = 4.dp)
                .constrainAs(glycemicValue) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(guideline)
                },
            contentAlignment = Alignment.Center
        ) {
            AppText(
                text = value.toString(),
                textStyle = Typography.labelMedium,
                color = White
            )
        }

        AppText(
            modifier = Modifier.constrainAs(glycemicEvaluate) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(glycemicValue.end)
                end.linkTo(parent.end)
                horizontalBias = 1f
            },
            text = stringResource(evaluate.evaluate),
            textStyle = Typography.labelMedium
        )
    }
}

@Composable
fun NutritionTable(
    modifier: Modifier = Modifier,
    nutrition: List<Nutrient>
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Color.Unspecified,
                shape = RoundedCornerShape(12.dp)
            )
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                color = GreyDADADA,
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        nutrition.forEachIndexed { index, nutrient ->
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(if (index % 2 == 0) White else GreyF4F5F5)
                    .padding(
                        vertical = 8.dp,
                        horizontal = 16.dp
                    )
            ) {
                val (name, amount, dailyPercentage) = createRefs()
                val amountGuideline = createGuidelineFromEnd(0.2f)

                AppText(
                    modifier = Modifier
                        .constrainAs(name) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                        },
                    text = nutrient.name,
                    textStyle = Typography.labelMedium
                )

                AppText(
                    modifier = Modifier
                        .constrainAs(amount) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            end.linkTo(amountGuideline)
                        },
                    text = stringResource(
                        R.string.recipe_health_score_nutrition_amount,
                        nutrient.amount,
                        nutrient.unit
                    ),
                    textStyle = Typography.bodyMedium,
                    textAlign = TextAlign.End
                )

                AppText(
                    modifier = Modifier.constrainAs(dailyPercentage) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    },
                    text = stringResource(
                        R.string.recipe_health_score_nutrition_daily_percentage,
                        nutrient.dailyPercentValue
                    ),
                    textStyle = Typography.bodyMedium,
                    color = Grey8A949F,
                    textAlign = TextAlign.End
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HealthSectionHeaderPreview() {
    HealthSectionHeader(
        label = stringResource(R.string.recipe_health_score_label),
        onShowInfoClick = {}
    )
}

@Preview
@Composable
fun HealthScoreCardPreview() {
    HealthScoreCard(
        healthScore = 69,
        glycemicIndex = 100,
        glycemicLoad = 10
    )
}

@Preview(showBackground = true)
@Composable
fun NutritionTablePreview() {
    NutritionTable(
        nutrition = listOf(
            Nutrient(
                name = "Carbs",
                amount = 35.5f,
                unit = "g",
                dailyPercentValue = 3
            ),
            Nutrient(
                name = "Carbs",
                amount = 100.5f,
                unit = "g",
                dailyPercentValue = 100
            )
        )
    )
}
