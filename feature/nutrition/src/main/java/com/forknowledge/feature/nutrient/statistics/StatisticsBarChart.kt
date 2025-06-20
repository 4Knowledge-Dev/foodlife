package com.forknowledge.feature.nutrient.statistics

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.forknowledge.core.common.AppConstant.RECIPE_NUTRIENT_CALORIES_INDEX
import com.forknowledge.core.common.extension.toDayMonthString
import com.forknowledge.core.ui.theme.Green91C747
import com.forknowledge.core.ui.theme.Typography
import com.forknowledge.feature.model.userdata.IntakeNutrition
import com.forknowledge.feature.model.userdata.NutrientData
import com.himanshoe.charty.bar.BarChart
import com.himanshoe.charty.bar.config.BarChartColorConfig
import com.himanshoe.charty.bar.config.BarChartConfig
import com.himanshoe.charty.bar.config.BarTooltip
import com.himanshoe.charty.bar.model.BarData
import com.himanshoe.charty.common.LabelConfig
import com.himanshoe.charty.common.TargetConfig
import com.himanshoe.charty.common.asSolidChartColor
import java.util.Date

@Composable
fun StatisticsBarChart(
    modifier: Modifier = Modifier,
    target: Float,
    records: List<IntakeNutrition>,
    barColor: Color
) {
    val data = records.map { record ->
        BarData(
            xValue = record.date.toDayMonthString(),
            yValue = record.nutrients[RECIPE_NUTRIENT_CALORIES_INDEX].amount.toFloat()
        )
    }

    BarChart(
        modifier = modifier
            .height(350.dp)
            .fillMaxWidth(),
        target = if (target <= data.maxOf { it.yValue }) target else null,
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
fun StatisticsBarChartPreview()
{
    StatisticsBarChart(
        target = 1000f,
        barColor = Green91C747,
        records = listOf(
            IntakeNutrition(
                date = Date(),
                nutrients = listOf(
                    NutrientData(
                        name = "Calories",
                        amount = 1000f,
                        unit = "kcal"
                    )
                )
            ),
            IntakeNutrition(
                date = Date(),
                nutrients = listOf(
                    NutrientData(
                        name = "Calories",
                        amount = 750f,
                        unit = "kcal"
                    )
                )
            )
        )
    )
}
