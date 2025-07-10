package com.forknowledge.feature.nutrient.statistics.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.forknowledge.core.common.extension.toDayMonthString
import com.forknowledge.core.data.model.StatisticsNutrientRecord
import com.forknowledge.core.ui.theme.Green91C747
import com.forknowledge.core.ui.theme.Grey808993
import com.forknowledge.core.ui.theme.OrangeFB880C
import com.forknowledge.core.ui.theme.Typography
import com.forknowledge.core.ui.theme.component.AppText
import com.forknowledge.feature.model.userdata.NutrientData
import com.forknowledge.feature.nutrient.R
import com.himanshoe.charty.bar.BarChart
import com.himanshoe.charty.bar.config.BarChartColorConfig
import com.himanshoe.charty.bar.config.BarChartConfig
import com.himanshoe.charty.bar.config.BarTooltip
import com.himanshoe.charty.bar.model.BarData
import com.himanshoe.charty.common.LabelConfig
import com.himanshoe.charty.common.TargetConfig
import com.himanshoe.charty.common.asSolidChartColor
import java.util.Date
import kotlin.math.roundToInt

@Composable
fun DietaryEnergy(
    target: Float = 0f,
    barColor: Color,
    nutritionRecords: List<StatisticsNutrientRecord>
) {
    val average = nutritionRecords.sumOf {
        it.nutrient.amount.roundToInt()
    }.toFloat() / nutritionRecords.size

    Column(modifier = Modifier.fillMaxWidth()) {
        AppText(
            text = stringResource(R.string.statistics_daily_label),
            textStyle = Typography.labelMedium,
            color = Grey808993
        )

        Row(
            modifier = Modifier.padding(top = 8.dp)
        ) {
            AppText(
                text = stringResource(R.string.statistics_daily_average_calories_label),
                textStyle = Typography.bodyLarge,
            )

            AppText(
                modifier = Modifier.padding(start = 8.dp),
                text = stringResource(
                    R.string.statistics_daily_average_calories_value,
                    average,
                    nutritionRecords[0].nutrient.unit
                ),
                textStyle = Typography.labelLarge,
                color = barColor
            )
        }

        if (target > 0) {
            Row(
                modifier = Modifier.padding(top = 8.dp)
            ) {
                AppText(
                    text = stringResource(R.string.statistics_calories_goal_label),
                    textStyle = Typography.bodyLarge,
                )

                AppText(
                    modifier = Modifier.padding(start = 8.dp),
                    text = stringResource(
                        R.string.statistics_calories_goal_value,
                        target.roundToInt(),
                        nutritionRecords[0].nutrient.unit
                    ),
                    textStyle = Typography.labelLarge,
                    color = Green91C747
                )
            }
        }

        val data = nutritionRecords.map { record ->
            BarData(
                xValue = record.date.toDayMonthString(),
                yValue = record.nutrient.amount
            )
        }

        StatisticsBarChart(
            modifier = Modifier.padding(top = 32.dp),
            target = target,
            data = data,
            barColor = barColor
        )
    }
}

@Composable
fun StatisticsBarChart(
    modifier: Modifier = Modifier,
    target: Float,
    data: List<BarData>,
    barColor: Color
) {
    BarChart(
        modifier = modifier
            .height(350.dp)
            .fillMaxWidth(),
        target = if (target > 0 && target <= data.maxOf { it.yValue }) target else null,
        data = { data },
        barTooltip = BarTooltip.GraphTop,
        targetConfig = TargetConfig.default().copy(

        ),
        labelConfig = LabelConfig.default().copy(
            showXLabel = true,
            showYLabel = true,
            xAxisCharCount = 5,
            labelTextStyle = Typography.bodySmall
        ),
        barChartColorConfig = BarChartColorConfig.default().copy(
            fillBarColor = barColor.asSolidChartColor()
        ),
        barChartConfig = BarChartConfig.default().copy(
            showCurvedBar = true
        ),
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun DietaryEnergyPreview() {
    DietaryEnergy(
        target = 2000f,
        barColor = OrangeFB880C,
        nutritionRecords = listOf(
            StatisticsNutrientRecord(
                date = Date(),
                nutrient = NutrientData(
                    name = "Calories",
                    amount = 1000f,
                    unit = "kcal"
                )
            ),
            StatisticsNutrientRecord(
                date = Date(),
                nutrient = NutrientData(
                    name = "Calories",
                    amount = 1200f,
                    unit = "kcal"
                )
            )
        )
    )
}
