package com.forknowledge.feature.nutrient.statistics

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.forknowledge.core.common.AppConstant.RECIPE_NUTRIENT_CALORIES_INDEX
import com.forknowledge.core.ui.theme.Green91C747
import com.forknowledge.core.ui.theme.Typography
import com.forknowledge.core.ui.theme.OrangeFF9524
import com.forknowledge.core.ui.theme.OrangeFB880C
import com.forknowledge.core.ui.theme.component.AppText
import com.forknowledge.feature.model.userdata.IntakeNutrition
import com.forknowledge.feature.model.userdata.NutrientData
import com.forknowledge.feature.nutrient.R
import kotlin.math.roundToInt

@Composable
fun DietaryEnergy(
    modifier: Modifier = Modifier,
    target: Float,
    nutritionRecords: List<IntakeNutrition>
) {
    val average = nutritionRecords.sumOf {
        it.nutrients[RECIPE_NUTRIENT_CALORIES_INDEX].amount.roundToInt()
    } / nutritionRecords.size

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(top = 8.dp)
        ) {
            AppText(
                text = stringResource(R.string.statistics_daily_average_calories_label),
                textStyle = Typography.bodyLarge,
            )

            AppText(
                modifier = Modifier.padding(start = 8.dp),
                text = stringResource(R.string.statistics_daily_average_calories_value, average),
                textStyle = Typography.labelLarge,
                color = OrangeFB880C
            )
        }

        Row(
            modifier = Modifier.padding(top = 8.dp)
        ) {
            AppText(
                text = stringResource(R.string.statistics_calories_goal_label),
                textStyle = Typography.bodyLarge,
            )

            AppText(
                modifier = Modifier.padding(start = 8.dp),
                text = stringResource(R.string.statistics_calories_goal_value, target.roundToInt()),
                textStyle = Typography.labelLarge,
                color = Green91C747
            )
        }

        StatisticsBarChart(
            modifier = Modifier.padding(top = 40.dp),
            target = target,
            records = nutritionRecords,
            barColor = OrangeFF9524
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun DietaryEnergyPreview() {
    DietaryEnergy(
        target = 2000f,
        nutritionRecords = listOf(
            IntakeNutrition(
                nutrients = listOf(
                    NutrientData(
                        name = "Calories",
                        amount = 1000f,
                    )
                )
            )
        )
    )
}
