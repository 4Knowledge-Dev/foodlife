package com.forknowledge.feature.nutrient.statistics

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.forknowledge.core.common.extension.toDateMonthString
import com.forknowledge.core.data.model.StatisticsNutrientRecord
import com.forknowledge.core.ui.R.drawable
import com.forknowledge.core.ui.theme.Black374957
import com.forknowledge.core.ui.theme.GreenA1CE50
import com.forknowledge.core.ui.theme.Grey7F000000
import com.forknowledge.core.ui.theme.GreyEBEBEB
import com.forknowledge.core.ui.theme.OrangeFF9524
import com.forknowledge.core.ui.theme.Typography
import com.forknowledge.core.ui.theme.component.AppText
import com.forknowledge.core.ui.theme.component.ErrorMessage
import com.forknowledge.core.ui.theme.component.LoadingIndicator
import com.forknowledge.feature.model.userdata.NutrientData
import com.forknowledge.feature.nutrient.R
import com.forknowledge.feature.nutrient.StatisticsTabRow
import com.forknowledge.feature.nutrient.NutritionGroupType
import com.forknowledge.feature.nutrient.statistics.component.DietaryEnergy
import java.util.Date
import kotlin.math.roundToInt

@Composable
fun StatisticsScreen(
    viewModel: StatisticsViewModel = hiltViewModel(),
    nutritionName: String,
    nutritionType: NutritionGroupType,
    onNavigateBack: () -> Unit
) {
    var selectedTabIndex by remember {
        mutableIntStateOf(StatisticsTabRow.DAILY.ordinal)
    }

    val shouldShowLoading = viewModel.shouldShowLoading
    val shouldShowError = viewModel.shouldShowError
    val statisticsData by viewModel.statisticsData.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getStatisticsData(nutritionName)
    }

    Scaffold(
        topBar = {
            StatisticsTopAppBar(
                title = nutritionName,
                selected = selectedTabIndex,
                onTabChanged = { selectedTabIndex = it },
                onNavigateBack = onNavigateBack
            )
        }
    ) { innerPadding ->
        if (shouldShowLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LoadingIndicator()
            }
        }

        if (shouldShowError) {
            ErrorMessage(stringResource(R.string.nutrient_insights_error_message))
        }

        if (!shouldShowLoading && !shouldShowError) {
            if (statisticsData.records.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .background(White)
                        .verticalScroll(rememberScrollState())
                        .padding(
                            vertical = 24.dp,
                            horizontal = 24.dp
                        )
                ) {
                    val targetNutrition = when (nutritionType) {
                        NutritionGroupType.ENERGY -> statisticsData.targetCalories.toFloat()
                        NutritionGroupType.CARB -> statisticsData.targetCarbs
                        NutritionGroupType.PROTEIN -> statisticsData.targetProteins
                        NutritionGroupType.FAT -> statisticsData.targetFats
                        else -> 0f
                    }

                    DietaryEnergy(
                        target = targetNutrition,
                        barColor = nutritionType.color,
                        nutritionRecords = statisticsData.records
                    )

                    IntakeNutritionHistory(
                        textColor = nutritionType.color,
                        records = statisticsData.records
                    )
                }
            } else {
                NoRecordFoundMessage(stringResource(R.string.statistics_not_enough_data_message))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsTopAppBar(
    title: String,
    selected: Int,
    onTabChanged: (Int) -> Unit,
    onNavigateBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    painter = painterResource(id = drawable.ic_back),
                    tint = Black374957,
                    contentDescription = null
                )
            }

            AppText(
                modifier = Modifier.padding(start = 24.dp),
                text = title,
                textStyle = Typography.titleSmall
            )
        }

        SecondaryTabRow(
            selectedTabIndex = selected,
            containerColor = MaterialTheme.colorScheme.background,
            indicator = {
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier
                        .tabIndicatorOffset(
                            selectedTabIndex = selected,
                            matchContentSize = true
                        ),
                    height = 2.dp,
                    color = GreenA1CE50,
                )
            },
        ) {
            StatisticsTabRow.entries.forEachIndexed { index, tab ->
                Tab(
                    selected = index == selected,
                    text = {
                        AppText(
                            text = stringResource(tab.title),
                            textStyle = Typography.labelMedium
                        )
                    },
                    onClick = { onTabChanged(index) }
                )
            }
        }
    }
}

@Composable
fun IntakeNutritionHistory(
    textColor: Color,
    records: List<StatisticsNutrientRecord>
) {
    Column(
        modifier = Modifier
            .padding(top = 40.dp)
            .fillMaxWidth()
            .background(White)
    ) {
        AppText(
            text = stringResource(R.string.statistics_history_title),
            textStyle = Typography.titleSmall
        )

        records.reversed().forEachIndexed { index, record ->
            Row(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AppText(
                    text = record.date.toDateMonthString(),
                    textStyle = Typography.bodyLarge
                )

                AppText(
                    text = stringResource(
                        R.string.statistics_history_intake_nutrition_value,
                        record.nutrient.amount.roundToInt(),
                        record.nutrient.unit
                    ),
                    textStyle = Typography.labelLarge,
                    color = textColor
                )
            }

            if (index != records.lastIndex) {
                HorizontalDivider(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth(),
                    color = GreyEBEBEB
                )
            }
        }
    }
}

@Composable
fun NoRecordFoundMessage(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.size(250.dp),
                painter = painterResource(drawable.img_vector_no_data_found),
                contentDescription = null
            )

            AppText(
                text = message,
                textStyle = Typography.bodyLarge,
                color = Grey7F000000
            )
        }
    }
}

@Preview
@Composable
fun StatisticsTopAppBarPreview() {
    StatisticsTopAppBar(
        title = "Dietary intake",
        selected = 1,
        onTabChanged = {},
        onNavigateBack = {}
    )
}

@Preview
@Composable
fun IntakeNutritionHistoryPreview() {
    IntakeNutritionHistory(
        textColor = OrangeFF9524,
        records = listOf(
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

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun NoRecordFoundMessagePreview() {
    NoRecordFoundMessage("No data exists in history!")
}
