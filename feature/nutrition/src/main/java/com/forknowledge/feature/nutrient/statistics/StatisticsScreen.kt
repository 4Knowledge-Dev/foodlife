package com.forknowledge.feature.nutrient.statistics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.forknowledge.core.ui.R.drawable
import com.forknowledge.core.ui.theme.Black374957
import com.forknowledge.core.ui.theme.GreenA1CE50
import com.forknowledge.core.ui.theme.GreyEBEBEB
import com.forknowledge.core.ui.theme.OrangeFF9524
import com.forknowledge.core.ui.theme.Typography
import com.forknowledge.core.ui.theme.component.AppText
import com.forknowledge.feature.model.userdata.IntakeNutrition
import com.forknowledge.feature.model.userdata.NutrientData
import com.forknowledge.feature.nutrient.R
import com.forknowledge.feature.nutrient.StatisticsTabRow
import com.forknowledge.feature.nutrient.StatisticsType
import kotlin.math.roundToInt

@Composable
fun StatisticsScreen(
    viewModel: StatisticsViewModel = hiltViewModel(),
    nutritionType: StatisticsType = StatisticsType.ENERGY,
    onNavigateBack: () -> Unit
) {
    var selectedTabIndex by remember {
        mutableIntStateOf(StatisticsTabRow.DAILY.ordinal)
    }

    val statisticsData by viewModel.statisticsData.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            StatisticsTopAppBar(
                title = stringResource(nutritionType.title),
                selected = selectedTabIndex,
                onTabChanged = { selectedTabIndex = it },
                onNavigateBack = onNavigateBack
            )
        }
    ) { innerPadding ->
        statisticsData?.let { data ->
            if (data.records.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .background(White)
                        .verticalScroll(rememberScrollState())
                        .padding(
                            vertical = 24.dp,
                            horizontal = 16.dp
                        )
                ) {
                    when (nutritionType) {
                        StatisticsType.ENERGY -> {
                            DietaryEnergy(
                                target = data.targetCalories.toFloat(),
                                nutritionRecords = data.records
                            )
                        }

                        StatisticsType.CARB -> {}
                        StatisticsType.PROTEIN -> {}
                        StatisticsType.FAT -> {}
                    }

                    IntakeNutritionHistory(
                        nutritionIndex = nutritionType.nutritionIndex,
                        textColor = nutritionType.color,
                        records = data.records
                    )

                }
            } else {

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
                .padding(horizontal = 16.dp),
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
    nutritionIndex: Int,
    textColor: Color,
    records: List<IntakeNutrition>
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
                        record.nutrients[nutritionIndex].amount.roundToInt(),
                        record.nutrients[nutritionIndex].unit
                    ),
                    textStyle = Typography.labelLarge,
                    color = textColor
                )
            }

            if(index != records.lastIndex) {
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
        nutritionIndex = 0,
        textColor = OrangeFF9524,
        records = listOf(
            IntakeNutrition(
                nutrients = listOf(
                    NutrientData(
                        name = "Calories",
                        amount = 1000f,
                        unit = "kcal"
                    )
                )
            ),
            IntakeNutrition(
                nutrients = listOf(
                    NutrientData(
                        name = "Calories",
                        amount = 1000f,
                        unit = "kcal"
                    )
                )
            )
        )
    )
}
