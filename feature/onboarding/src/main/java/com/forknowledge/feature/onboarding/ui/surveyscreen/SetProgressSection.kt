package com.forknowledge.feature.onboarding.ui.surveyscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.forknowledge.core.common.healthtype.Goal
import com.forknowledge.core.common.healthtype.Macros
import com.forknowledge.core.common.healthtype.NutrientType
import com.forknowledge.core.common.nutrientRatioToNutrientAmount
import com.forknowledge.core.ui.R.drawable
import com.forknowledge.core.ui.theme.Black374957
import com.forknowledge.core.ui.theme.Blue05A6F1
import com.forknowledge.core.ui.theme.Blue6ABFFF
import com.forknowledge.core.ui.theme.BlueD9EBFF
import com.forknowledge.core.ui.theme.Green86BF3E
import com.forknowledge.core.ui.theme.Grey808993
import com.forknowledge.core.ui.theme.RedF44336
import com.forknowledge.core.ui.theme.RedFF4950
import com.forknowledge.core.ui.theme.Typography
import com.forknowledge.core.ui.theme.YellowFFAE01
import com.forknowledge.core.ui.theme.component.AppText
import com.forknowledge.feature.onboarding.R
import com.himanshoe.charty.common.asSolidChartColor
import com.himanshoe.charty.pie.PieChart
import com.himanshoe.charty.pie.model.PieChartData
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetProgressSection(
    tdee: Int,
    targetCalories: Int,
    goal: Goal,
    targetWeightPerWeek: Double,
    macro: Macros,
    onUpdateTargetWeightPerWeek: (Double) -> Unit
) {
    var progress by remember { mutableFloatStateOf(targetWeightPerWeek.toFloat()) }
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    if (showBottomSheet) {
        ModalBottomSheet(
            sheetState = sheetState,
            containerColor = Color.White,
            onDismissRequest = { showBottomSheet = false }
        ) {
            InformationBottomSheet(
                title = stringResource(id = R.string.onboarding_survey_progress_bottom_sheet_tdee_title),
                description = stringResource(id = R.string.onboarding_survey_progress_bottom_sheet_tdee_description)
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(
                vertical = 24.dp,
                horizontal = 16.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppText(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.onboarding_survey_progress_macro_title),
            textStyle = Typography.titleSmall,
            color = Black374957,
            textAlign = TextAlign.Center
        )

        Box(
            modifier = Modifier
                .padding(top = 32.dp)
                .size(200.dp),
            contentAlignment = Alignment.Center
        ) {
            PieChart(
                modifier = Modifier.fillMaxSize(),
                data = {
                    listOf(
                        PieChartData(
                            label = macro.carbs.toString(),
                            value = macro.carbs.toFloat(),
                            color = RedFF4950.asSolidChartColor()
                        ),
                        PieChartData(
                            label = macro.protein.toString(),
                            value = macro.protein.toFloat(),
                            color = YellowFFAE01.asSolidChartColor()
                        ),
                        PieChartData(
                            label = macro.fat.toString(),
                            value = macro.fat.toFloat(),
                            color = Blue05A6F1.asSolidChartColor()
                        )
                    )
                },
                isDonutChart = true
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                AppText(
                    text = targetCalories.toString(),
                    textStyle = Typography.titleLarge,
                    color = Black374957
                )

                AppText(
                    text = stringResource(id = R.string.onboarding_survey_progress_calories_unit),
                    textStyle = Typography.bodyLarge,
                    color = Grey808993
                )
            }
        }

        Column(modifier = Modifier.padding(top = 24.dp)) {
            NutrientMacro(
                nutrientType = NutrientType.CARBOHYDRATE,
                ratio = macro.carbs,
                targetCalories = targetCalories
            )

            NutrientMacro(
                nutrientType = NutrientType.PROTEIN,
                ratio = macro.protein,
                targetCalories = targetCalories
            )

            NutrientMacro(
                nutrientType = NutrientType.FAT,
                ratio = macro.fat,
                targetCalories = targetCalories
            )
        }

        if (goal != Goal.EAT_HEALTHY) {
            AppText(
                modifier = Modifier
                    .padding(top = 36.dp)
                    .fillMaxWidth(),
                text = stringResource(id = R.string.onboarding_survey_progress_title),
                textStyle = Typography.titleSmall,
                color = Black374957,
                textAlign = TextAlign.Center
            )
        }

        // TDEE
        CalculateCalories(
            modifier = Modifier.padding(top = 24.dp),
            label = stringResource(id = R.string.onboarding_survey_progress_tdee_label),
            calories = tdee,
            isToolTipVisible = true,
            onTooltipsClick = { showBottomSheet = true }
        )

        // Calories difference
        if (goal != Goal.EAT_HEALTHY) {
            CalculateCalories(
                modifier = Modifier.padding(top = 4.dp),
                label = stringResource(
                    when (goal) {
                        Goal.LOSE_WEIGHT -> R.string.onboarding_survey_progress_calories_deficit_label
                        else -> R.string.onboarding_survey_progress_calories_exceed_label
                    }
                ),
                calories = abs(tdee - targetCalories),
                onTooltipsClick = {}
            )
        }

        // Calories target
        CalculateCalories(
            modifier = Modifier.padding(top = 16.dp),
            label = stringResource(id = R.string.onboarding_survey_progress_calories_target_label),
            calories = targetCalories,
            color = RedF44336,
            onTooltipsClick = {}
        )

        if (goal != Goal.EAT_HEALTHY) {
            AppText(
                modifier = Modifier
                    .padding(top = 36.dp)
                    .fillMaxWidth(),
                text = stringResource(
                    R.string.onboarding_survey_progress_text_target_calories_per_week,
                    progress
                ),
                textStyle = Typography.labelLarge,
                color = Black374957,
                textAlign = TextAlign.Center
            )

            Slider(
                modifier = Modifier.padding(top = 8.dp),
                value = progress,
                valueRange = 0.1f..1f,
                steps = 10,
                colors = SliderDefaults.colors(
                    thumbColor = Green86BF3E,
                    activeTrackColor = Green86BF3E,
                    inactiveTrackColor = BlueD9EBFF,
                    inactiveTickColor = Blue6ABFFF
                ),
                onValueChange = { targetWeight ->
                    progress = targetWeight
                    onUpdateTargetWeightPerWeek(targetWeight.toDouble())
                }
            )

            AppText(
                modifier = Modifier.padding(top = 16.dp),
                text = stringResource(R.string.onboarding_survey_progress_target_calories_per_week_note),
                textStyle = Typography.bodySmall,
                color = Grey808993
            )
        }
    }
}

@Composable
fun NutrientMacro(
    nutrientType: NutrientType,
    ratio: Double,
    targetCalories: Int
) {
    Row(
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val nutrientLabel = when (nutrientType) {
            NutrientType.CARBOHYDRATE -> stringResource(id = R.string.onboarding_survey_diet_carbs)
            NutrientType.PROTEIN -> stringResource(id = R.string.onboarding_survey_diet_protein)
            NutrientType.FAT -> stringResource(id = R.string.onboarding_survey_diet_fat)
        }

        val nutrientAmount = when (nutrientType) {
            NutrientType.CARBOHYDRATE -> nutrientRatioToNutrientAmount(
                nutrient = nutrientType,
                totalCalories = targetCalories,
                ratio = ratio
            )

            NutrientType.PROTEIN -> nutrientRatioToNutrientAmount(
                nutrient = nutrientType,
                totalCalories = targetCalories,
                ratio = ratio
            )

            NutrientType.FAT -> nutrientRatioToNutrientAmount(
                nutrient = nutrientType,
                totalCalories = targetCalories,
                ratio = ratio
            )
        }

        Box(
            modifier = Modifier
                .size(10.dp)
                .background(
                    color = when (nutrientType) {
                        NutrientType.CARBOHYDRATE -> RedFF4950
                        NutrientType.PROTEIN -> YellowFFAE01
                        NutrientType.FAT -> Blue05A6F1
                    },
                    shape = CircleShape
                )
        )

        AppText(
            modifier = Modifier.padding(start = 12.dp),
            text = nutrientLabel,
            textStyle = Typography.bodyMedium,
            color = Black374957
        )

        Spacer(modifier = Modifier.weight(1f))

        AppText(
            text = stringResource(
                R.string.onboarding_survey_progress_macro_value,
                nutrientAmount,
                (ratio * 100).toInt()
            ),
            textStyle = Typography.labelMedium,
            color = Black374957
        )
    }
}

@Composable
fun CalculateCalories(
    modifier: Modifier = Modifier,
    label: String,
    calories: Int,
    color: Color = Black374957,
    isToolTipVisible: Boolean = false,
    onTooltipsClick: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AppText(
            text = label,
            textStyle = Typography.bodyMedium,
            color = color
        )

        if (isToolTipVisible) {
            IconButton(
                modifier = Modifier.padding(start = 4.dp),
                onClick = { onTooltipsClick() }
            ) {
                Icon(
                    painter = painterResource(id = drawable.ic_tooltips),
                    tint = Black374957,
                    contentDescription = null
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        AppText(
            text = stringResource(id = R.string.onboarding_survey_progress_text_calories, calories),
            textStyle = Typography.labelMedium,
            color = color
        )
    }
}

@Composable
fun InformationBottomSheet(
    title: String,
    description: String
) {
    Column(
        modifier = Modifier
            .padding(
                vertical = 24.dp,
                horizontal = 16.dp
            )
            .fillMaxWidth()
    ) {
        AppText(
            text = title,
            textStyle = Typography.labelLarge
        )

        AppText(
            modifier = Modifier.padding(top = 8.dp),
            text = description,
            textStyle = Typography.bodyMedium
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun CalculateCaloriesPreview() {
    CalculateCalories(
        label = "Calories deficit",
        calories = 2000,
        onTooltipsClick = {}
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun InformationBottomSheetPreview() {
    InformationBottomSheet(
        title = "Calories deficit",
        description = "Calories deficit"
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun SetProgressSectionPreview() {
    SetProgressSection(
        tdee = 2000,
        targetCalories = 2000,
        targetWeightPerWeek = 0.5,
        goal = Goal.LOSE_WEIGHT,
        macro = Macros(
            carbs = 0.5,
            protein = 0.3,
            fat = 0.2
        ),
        onUpdateTargetWeightPerWeek = {}
    )
}