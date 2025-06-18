package com.forknowledge.feature.nutrient.insights

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
import com.forknowledge.core.ui.theme.GreenD8E4CD
import com.forknowledge.core.ui.theme.GreyF4F5F5
import com.forknowledge.core.ui.theme.GreyFAFAFA
import com.forknowledge.core.ui.theme.RedFF4950
import com.forknowledge.core.ui.theme.Typography
import com.forknowledge.core.ui.theme.YellowFB880C
import com.forknowledge.core.ui.theme.component.AppText
import com.forknowledge.core.ui.theme.component.LoadingIndicator
import com.forknowledge.feature.model.userdata.NutrientData
import com.forknowledge.feature.nutrient.R
import com.himanshoe.charty.common.asSolidChartColor
import com.himanshoe.charty.pie.PieChart
import com.himanshoe.charty.pie.model.PieChartData
import java.util.Date
import kotlin.math.roundToInt

@Composable
fun InsightsScreen(
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
            ErrorMessageSection()
        }

        dailyNutrition?.let { nutrition ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(GreyF4F5F5)
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

                NutrientInfoSection(nutrients = nutrition.nutrients)
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

        nutrients.forEachIndexed { index, nutrient ->
            var progress by remember { mutableFloatStateOf(0f) }
            val animatedProgress by animateFloatAsState(
                targetValue = progress.toFloat(),
                animationSpec = tween(durationMillis = 1000)
            )
            val target = when (index) {
                RECIPE_NUTRIENT_CALORIES_INDEX -> targetCalories
                RECIPE_NUTRIENT_CARB_INDEX -> (targetCalories * targetCarbsRatio).roundToInt()
                RECIPE_NUTRIENT_PROTEIN_INDEX -> (targetCalories * targetProteinRatio).roundToInt()
                RECIPE_NUTRIENT_FAT_INDEX -> (targetCalories * targetFatRatio).roundToInt()
                else -> 0
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
                trackColor = GreenD8E4CD,
                gapSize = (-4).dp,
                drawStopIndicator = {
                    drawStopIndicator(
                        drawScope = this,
                        stopSize = ProgressIndicatorDefaults.LinearTrackStopIndicatorSize,
                        color = if (nutrient.amount.roundToInt() >= target) White else GreenD8E4CD,
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
    val totalCalories = nutrientAmountToCalories(
        nutrient = NutrientType.CARBOHYDRATE,
        amount = nutrients[RECIPE_NUTRIENT_CARB_INDEX].amount
    ) + nutrientAmountToCalories(
        nutrient = NutrientType.PROTEIN,
        amount = nutrients[RECIPE_NUTRIENT_PROTEIN_INDEX].amount
    ) + nutrientAmountToCalories(
        nutrient = NutrientType.FAT,
        amount = nutrients[RECIPE_NUTRIENT_FAT_INDEX].amount
    )

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
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

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
                    color = YellowFB880C.asSolidChartColor(),
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

            PieChart(
                modifier = Modifier.size(150.dp),
                data = { targetRatioData }
            )

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
                        color = YellowFB880C.asSolidChartColor(),
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

                PieChart(
                    modifier = Modifier.size(150.dp),
                    data = { actualRatioData }
                )
            } else {
                val actualRatioData = listOf(
                    PieChartData(
                        value = 0.33f,
                        color = GreyFAFAFA.asSolidChartColor(),
                        label = ""
                    ),
                    PieChartData(
                        value = 0.33f,
                        color = YellowFB880C.asSolidChartColor(),
                        label = ""
                    ),
                    PieChartData(
                        value = 0.33f,
                        color = Blue05A6F1.asSolidChartColor(),
                        label = ""
                    ),
                )
                PieChart(
                    modifier = Modifier.size(150.dp),
                    data = { actualRatioData }
                )
            }
        }
    }
}

@Composable
fun NutrientInfoSection(
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

        nutrients.forEach { nutrient ->
            Row(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AppText(
                    text = nutrient.name,
                    textStyle = Typography.labelLarge
                )

                AppText(
                    text = stringResource(
                        R.string.nutrient_insights_amount_unit,
                        nutrient.amount,
                        nutrient.unit
                    ),
                    textStyle = Typography.labelLarge
                )
            }
        }
    }

}

@Composable
fun ErrorMessageSection() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier.size(150.dp),
            painter = painterResource(id = drawable.img_vector_internet_error),
            contentScale = ContentScale.Crop,
            contentDescription = null
        )

        AppText(
            text = stringResource(R.string.nutrient_insights_error_message),
            textStyle = Typography.bodyMedium
        )
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
        nutrients = List(4) { NutrientData() }
        /*nutrients = listOf(
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
        )*/
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

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun ErrorMessageSectionPreview() {
    ErrorMessageSection()
}
