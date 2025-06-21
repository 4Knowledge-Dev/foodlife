package com.forknowledge.feature.nutrient.dailyinsights

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.ProgressIndicatorDefaults.drawStopIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.forknowledge.core.common.AppConstant.NUTRIENT_CALORIES_NAME
import com.forknowledge.core.common.AppConstant.NUTRIENT_CARB_NAME
import com.forknowledge.core.common.AppConstant.NUTRIENT_FAT_NAME
import com.forknowledge.core.common.AppConstant.NUTRIENT_PROTEIN_NAME
import com.forknowledge.core.common.AppConstant.RECIPE_NUTRIENT_CALORIES_INDEX
import com.forknowledge.core.common.AppConstant.RECIPE_NUTRIENT_CARB_INDEX
import com.forknowledge.core.common.AppConstant.RECIPE_NUTRIENT_FAT_INDEX
import com.forknowledge.core.common.AppConstant.RECIPE_NUTRIENT_PROTEIN_INDEX
import com.forknowledge.core.common.extension.toDayMonthDateString
import com.forknowledge.core.common.healthtype.NutrientType
import com.forknowledge.core.common.nutrientAmountToCalories
import com.forknowledge.core.common.nutrientAmountToCaloriesRatio
import com.forknowledge.core.ui.R.drawable
import com.forknowledge.core.ui.theme.Black374957
import com.forknowledge.core.ui.theme.Blue05A6F1
import com.forknowledge.core.ui.theme.Green91C747
import com.forknowledge.core.ui.theme.GreenE2F2EC
import com.forknowledge.core.ui.theme.GreyF4F5F5
import com.forknowledge.core.ui.theme.GreyFAFAFA
import com.forknowledge.core.ui.theme.RedFF4950
import com.forknowledge.core.ui.theme.Typography
import com.forknowledge.core.ui.theme.YellowFFAE01
import com.forknowledge.core.ui.theme.component.AppText
import com.forknowledge.core.ui.theme.component.ErrorMessage
import com.forknowledge.core.ui.theme.component.LoadingIndicator
import com.forknowledge.feature.model.userdata.NutrientData
import com.forknowledge.feature.nutrient.R
import com.himanshoe.charty.common.asSolidChartColor
import com.himanshoe.charty.pie.PieChart
import com.himanshoe.charty.pie.model.PieChartData
import java.util.Date
import kotlin.math.roundToInt

@Composable
fun DailyInsightsScreen(
    viewModel: InsightsViewModel = hiltViewModel(),
    dateInMillis: Long,
    onNavigateBack: () -> Unit
) {
    val dailyNutrition by viewModel.userNutritionRecord.collectAsStateWithLifecycle()
    val shouldShowLoading = viewModel.shouldShowLoading
    val shouldShowError = viewModel.shouldShowError

    LaunchedEffect(Unit) {
        viewModel.getDailyNutritionRecord(Date(dateInMillis))
    }

    Scaffold(
        topBar = {
            InsightsTopBar(
                date = Date(dateInMillis),
                onNavigateBack = onNavigateBack
            )
        }
    ) { innerPadding ->
        if (shouldShowLoading) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                LoadingIndicator()
            }
        }

        if (shouldShowError) {
            ErrorMessage(stringResource(R.string.nutrient_insights_error_message))
        }

        dailyNutrition?.let { nutrition ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(GreyF4F5F5)
                    .verticalScroll(rememberScrollState())
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DailyProgressSection(
                    targetCalories = nutrition.targetCalories,
                    targetCarbsRatio = nutrition.targetCarbRatio,
                    targetProteinRatio = nutrition.targetProteinRatio,
                    targetFatRatio = nutrition.targetFatRatio,
                    nutrients = nutrition.nutrients
                )

                MacroSection(
                    targetCarbRatio = nutrition.targetCarbRatio,
                    targetProteinRatio = nutrition.targetProteinRatio,
                    targetFatRatio = nutrition.targetFatRatio,
                    nutrients = nutrition.nutrients
                )

                if (nutrition.nutrients.isNotEmpty()) {
                    NutrientInfoSection(nutrients = nutrition.nutrients)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsightsTopBar(
    date: Date,
    onNavigateBack: () -> Unit
) {
    TopAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
            .padding(horizontal = 16.dp),
        title = {
            AppText(
                modifier = Modifier.padding(start = 32.dp),
                text = date.toDayMonthDateString(),
                textStyle = Typography.labelLarge
            )
        },
        navigationIcon = {
            Icon(
                modifier = Modifier.clickable { onNavigateBack() },
                painter = painterResource(drawable.ic_back),
                tint = Black374957,
                contentDescription = null
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = White
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyProgressSection(
    targetCalories: Int,
    targetCarbsRatio: Float,
    targetProteinRatio: Float,
    targetFatRatio: Float,
    nutrients: List<NutrientData>
) {
    Column(
        modifier = Modifier
            .padding(
                top = 24.dp,
                start = 16.dp,
                end = 16.dp
            )
            .fillMaxWidth()
            .background(
                color = Green91C747,
                shape = RoundedCornerShape(24.dp)
            )
            .padding(
                top = 13.dp,
                bottom = 21.dp,
                start = 21.dp,
                end = 21.dp
            )
    ) {
        AppText(
            modifier = Modifier.padding(bottom = 8.dp),
            text = stringResource(R.string.nutrient_insights_goal_label),
            textStyle = Typography.titleSmall,
            color = White
        )

        val mainNutrients = if (nutrients.isNotEmpty()) {
            nutrients.filter {
                it.name == NUTRIENT_CALORIES_NAME
                        || it.name == NUTRIENT_CARB_NAME
                        || it.name == NUTRIENT_PROTEIN_NAME
                        || it.name == NUTRIENT_FAT_NAME
            }
        } else {
            listOf(
                NutrientData(
                    name = stringResource(R.string.nutrient_label_calories),
                    amount = 0f,
                    unit = stringResource(R.string.nutrient_insights_daily_progress_unit_kcal)
                ),
                NutrientData(
                    name = stringResource(R.string.nutrient_label_carb),
                    amount = 0f,
                    unit = stringResource(R.string.nutrient_insights_daily_progress_unit_gram)
                ),
                NutrientData(
                    name = stringResource(R.string.nutrient_label_protein),
                    amount = 0f,
                    unit = stringResource(R.string.nutrient_insights_daily_progress_unit_gram)
                ),
                NutrientData(
                    name = stringResource(R.string.nutrient_label_fat),
                    amount = 0f,
                    unit = stringResource(R.string.nutrient_insights_daily_progress_unit_gram)
                ),
            )
        }

        mainNutrients.forEachIndexed { index, nutrient ->
            var progress by remember { mutableFloatStateOf(0f) }
            val animatedProgress by animateFloatAsState(
                targetValue = progress.toFloat(),
                animationSpec = tween(durationMillis = 1000)
            )
            val target = when (index) {
                0 -> targetCalories
                1 -> (targetCalories * targetCarbsRatio).roundToInt()
                2 -> (targetCalories * targetProteinRatio).roundToInt()
                3 -> (targetCalories * targetFatRatio).roundToInt()
                else -> 1
            }

            LaunchedEffect(nutrient) {
                progress = nutrient.amount / target
            }

            Row(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AppText(
                    text = nutrient.name,
                    textStyle = Typography.bodySmall,
                    color = White
                )

                AppText(
                    modifier = Modifier.padding(top = 2.dp),
                    text = stringResource(
                        R.string.nutrient_insights_daily_progress,
                        nutrient.amount.roundToInt(),
                        target,
                        nutrient.unit
                    ),
                    textStyle = Typography.bodySmall,
                    color = White
                )
            }

            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp),
                progress = { animatedProgress },
                color = White,
                trackColor = GreenE2F2EC,
                gapSize = (-4).dp,
                drawStopIndicator = {
                    drawStopIndicator(
                        drawScope = this,
                        stopSize = ProgressIndicatorDefaults.LinearTrackStopIndicatorSize,
                        color = if (nutrient.amount.roundToInt() >= target) White else GreenE2F2EC,
                        strokeCap = StrokeCap.Round
                    )
                }
            )
        }
    }
}

@Composable
fun MacroSection(
    targetCarbRatio: Float,
    targetProteinRatio: Float,
    targetFatRatio: Float,
    nutrients: List<NutrientData>
) {
    val totalCalories = if (nutrients.isNotEmpty()) {
        nutrientAmountToCalories(
            nutrient = NutrientType.CARBOHYDRATE,
            amount = nutrients[RECIPE_NUTRIENT_CARB_INDEX].amount
        ) + nutrientAmountToCalories(
            nutrient = NutrientType.PROTEIN,
            amount = nutrients[RECIPE_NUTRIENT_PROTEIN_INDEX].amount
        ) + nutrientAmountToCalories(
            nutrient = NutrientType.FAT,
            amount = nutrients[RECIPE_NUTRIENT_FAT_INDEX].amount
        )
    } else {
        0f
    }

    Column(
        modifier = Modifier
            .padding(
                top = 24.dp,
                start = 16.dp,
                end = 16.dp
            )
            .fillMaxWidth()
            .background(
                color = White,
                shape = RoundedCornerShape(24.dp)
            )
            .clip(RoundedCornerShape(24.dp))
            .padding(21.dp)
    ) {
        AppText(
            text = stringResource(R.string.nutrient_insights_macro_info),
            textStyle = Typography.labelLarge
        )

        Row(
            modifier = Modifier
                .padding(top = 12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            MacroNote(
                label = stringResource(R.string.nutrient_label_carb),
                color = RedFF4950
            )

            MacroNote(
                label = stringResource(R.string.nutrient_label_protein),
                color = YellowFFAE01
            )

            MacroNote(
                label = stringResource(R.string.nutrient_label_fat),
                color = Blue05A6F1
            )
        }

        Row(
            modifier = Modifier
                .padding(top = 12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val targetRatioData = listOf(
                PieChartData(
                    value = targetCarbRatio,
                    color = RedFF4950.asSolidChartColor(),
                    label = stringResource(
                        R.string.nutrient_insights_pie_chart_label,
                        (targetCarbRatio * 100).toInt()
                    )
                ),
                PieChartData(
                    value = targetProteinRatio,
                    color = YellowFFAE01.asSolidChartColor(),
                    label = stringResource(
                        R.string.nutrient_insights_pie_chart_label,
                        (targetProteinRatio * 100).toInt()
                    )
                ),
                PieChartData(
                    value = targetFatRatio,
                    color = Blue05A6F1.asSolidChartColor(),
                    label = stringResource(
                        R.string.nutrient_insights_pie_chart_label,
                        (targetFatRatio * 100).toInt()
                    )
                )
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                PieChart(
                    modifier = Modifier.size(150.dp),
                    data = { targetRatioData }
                )

                AppText(
                    modifier = Modifier.padding(top = 8.dp),
                    text = stringResource(R.string.nutrient_insights_macro_recommend_label),
                    textStyle = Typography.bodyMedium
                )
            }

            if (totalCalories > 0) {
                val actualCarbRatio = nutrientAmountToCaloriesRatio(
                    nutrient = NutrientType.CARBOHYDRATE,
                    amount = nutrients[RECIPE_NUTRIENT_CARB_INDEX].amount,
                    totalCalories = totalCalories
                )
                val actualProteinRatio = nutrientAmountToCaloriesRatio(
                    nutrient = NutrientType.PROTEIN,
                    amount = nutrients[RECIPE_NUTRIENT_PROTEIN_INDEX].amount,
                    totalCalories = totalCalories
                )
                val actualFatRatio = 1 - actualCarbRatio - actualProteinRatio
                val actualFatPercent =
                    100 - (actualCarbRatio * 100).toInt() - (actualProteinRatio * 100).toInt()

                val actualRatioData = listOf(
                    PieChartData(
                        value = actualCarbRatio,
                        color = RedFF4950.asSolidChartColor(),
                        label = stringResource(
                            R.string.nutrient_insights_pie_chart_label,
                            (actualCarbRatio * 100).toInt()
                        )
                    ),
                    PieChartData(
                        value = actualProteinRatio,
                        color = YellowFFAE01.asSolidChartColor(),
                        label = stringResource(
                            R.string.nutrient_insights_pie_chart_label,
                            (actualProteinRatio * 100).toInt()
                        )
                    ),
                    PieChartData(
                        value = actualFatRatio,
                        color = Blue05A6F1.asSolidChartColor(),
                        label = stringResource(
                            R.string.nutrient_insights_pie_chart_label,
                            actualFatPercent
                        )
                    ),
                )

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    PieChart(
                        modifier = Modifier.size(150.dp),
                        data = { actualRatioData }
                    )

                    AppText(
                        modifier = Modifier.padding(top = 8.dp),
                        text = stringResource(R.string.nutrient_insights_macro_actual_label),
                        textStyle = Typography.bodyMedium
                    )
                }
            } else {
                val actualRatioData = listOf(
                    PieChartData(
                        value = 0.33f,
                        color = GreyFAFAFA.asSolidChartColor(),
                        label = ""
                    ),
                    PieChartData(
                        value = 0.33f,
                        color = GreyFAFAFA.asSolidChartColor(),
                        label = ""
                    ),
                    PieChartData(
                        value = 0.33f,
                        color = GreyFAFAFA.asSolidChartColor(),
                        label = ""
                    ),
                )

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    PieChart(
                        modifier = Modifier.size(150.dp),
                        data = { actualRatioData }
                    )

                    AppText(
                        modifier = Modifier.padding(top = 8.dp),
                        text = stringResource(R.string.nutrient_insights_macro_actual_label),
                        textStyle = Typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
fun MacroNote(
    label: String,
    color: Color
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(15.dp)
                .background(
                    color = color,
                    shape = CircleShape
                )
        )

        AppText(
            modifier = Modifier.padding(start = 8.dp),
            text = label,
            textStyle = Typography.bodySmall
        )
    }
}

@Composable
fun NutrientInfoSection(
    nutrients: List<NutrientData>
) {
    Column(
        modifier = Modifier
            .padding(
                vertical = 24.dp,
                horizontal = 16.dp
            )
            .fillMaxWidth()
            .background(
                color = White,
                shape = RoundedCornerShape(24.dp)
            )
            .clip(RoundedCornerShape(24.dp))
            .padding(21.dp)
    ) {
        AppText(
            modifier = Modifier.padding(bottom = 8.dp),
            text = stringResource(R.string.nutrient_insights_nutrition_info),
            textStyle = Typography.labelLarge
        )

        nutrients.forEachIndexed { index, nutrient ->
            val style = if (
                listOf(
                    RECIPE_NUTRIENT_CALORIES_INDEX,
                    RECIPE_NUTRIENT_CARB_INDEX,
                    RECIPE_NUTRIENT_PROTEIN_INDEX,
                    RECIPE_NUTRIENT_FAT_INDEX
                ).contains(index)
            ) {
                Typography.labelMedium
            } else {
                Typography.bodyMedium
            }

            Row(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AppText(
                    text = nutrient.name,
                    textStyle = style
                )

                AppText(
                    text = stringResource(
                        R.string.nutrient_insights_amount_unit,
                        nutrient.amount,
                        nutrient.unit
                    ),
                    textStyle = style
                )
            }
        }
    }

}

@Preview
@Composable
fun InsightsTopBarPreview() {
    InsightsTopBar(
        date = Date(),
        onNavigateBack = { }
    )
}

@Preview
@Composable
fun DailyProgressSectionPreview() {
    DailyProgressSection(
        targetCalories = 2000,
        targetCarbsRatio = 0.5f,
        targetProteinRatio = 0.3f,
        targetFatRatio = 0.2f,
        nutrients = listOf(
            NutrientData(
                name = "Calories",
                amount = 2000f,
                unit = "kcal"
            ),
            NutrientData(
                name = "Carbs",
                amount = 15f,
                unit = "g"
            ),
            NutrientData(
                name = "Protein",
                amount = 11f,
                unit = "g"
            ),
            NutrientData(
                name = "Fat",
                amount = 60f,
                unit = "g"
            ),
        )
    )
}

@Preview(backgroundColor = 0xFFFFFFFF, showBackground = true)
@Composable
fun MacroSectionPreview() {
    MacroSection(
        targetCarbRatio = 0.5f,
        targetProteinRatio = 0.3f,
        targetFatRatio = 0.2f,
        //nutrients = List(4) { NutrientData() }
        nutrients = listOf(
            NutrientData(
                name = "Carbs",
                amount = 2000f,
                unit = "g"
            ),
            NutrientData(
                name = "Carbs",
                amount = 155f,
                unit = "g"
            ),
            NutrientData(
                name = "Carbs",
                amount = 110f,
                unit = "g"
            ),
            NutrientData(
                name = "Carbs",
                amount = 60f,
                unit = "g"
            ),
        )
    )
}

@Preview(backgroundColor = 0xFFFFFFFF, showBackground = true)
@Composable
fun NutrientInfoSectionPreview() {
    NutrientInfoSection(
        listOf(
            NutrientData(
                name = "Carbs",
                amount = 100f,
                unit = "g"
            ),
            NutrientData(
                name = "Carbs",
                amount = 100f,
                unit = "g"
            ),
            NutrientData(
                name = "Carbs",
                amount = 100f,
                unit = "g"
            ),
        )
    )
}
