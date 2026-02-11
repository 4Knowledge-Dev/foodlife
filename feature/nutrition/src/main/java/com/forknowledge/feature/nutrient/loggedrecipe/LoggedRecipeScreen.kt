package com.forknowledge.feature.nutrient.loggedrecipe

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.forknowledge.core.ui.theme.Black374957
import com.forknowledge.core.ui.theme.Blue05A6F1
import com.forknowledge.core.ui.theme.Grey808993
import com.forknowledge.core.ui.theme.RedFF4950
import com.forknowledge.core.ui.theme.Typography
import com.forknowledge.core.ui.theme.YellowFFAE01
import com.forknowledge.core.ui.theme.component.AppText
import com.forknowledge.core.ui.theme.component.AppTopBar
import com.forknowledge.feature.nutrient.R
import com.himanshoe.charty.common.asSolidChartColor
import com.himanshoe.charty.pie.PieChart
import com.himanshoe.charty.pie.model.PieChartData
import kotlinx.serialization.Serializable

@Serializable
data class LoggedRecipeRoute(
    val meal: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoggedRecipeScreen(
    meal: String,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            AppTopBar(
                title = meal,
                onNavigateBack = onNavigateBack
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier.padding(innerPadding)
            ) { }
        }

    }
}

@Composable
fun MealMacro() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 28.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Box(
            modifier = Modifier
                .padding(top = 32.dp)
                .size(100.dp),
            contentAlignment = Alignment.Center
        ){
            PieChart(
                modifier = Modifier.fillMaxSize(),
                data = {
                    listOf(
                        PieChartData(
                            label = "",
                            value = 0.4f,
                            color = RedFF4950.asSolidChartColor()
                        ),
                        PieChartData(
                            label = "",
                            value = 0.4f,
                            color = YellowFFAE01.asSolidChartColor()
                        ),
                        PieChartData(
                            label = "",
                            value = 0.2f,
                            color = Blue05A6F1.asSolidChartColor()
                        )
                    )
                },
                isDonutChart = true
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                AppText(
                    text = "300",
                    textStyle = Typography.titleSmall,
                    color = Black374957
                )

                AppText(
                    text = stringResource(id = R.string.nutrient_activity_calories_unit),
                    textStyle = Typography.bodySmall,
                    color = Grey808993
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun MealMacroPreview() {
    MealMacro()
}


@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun LoggedRecipeScreenPreview() {
    LoggedRecipeScreen(
        meal = "Breakfast",
        onNavigateBack = {}
    )
}