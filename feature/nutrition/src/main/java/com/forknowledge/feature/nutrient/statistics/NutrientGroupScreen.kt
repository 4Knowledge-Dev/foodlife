package com.forknowledge.feature.nutrient.statistics

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.forknowledge.core.ui.R.drawable
import com.forknowledge.core.ui.theme.Black374957
import com.forknowledge.core.ui.theme.Typography
import com.forknowledge.core.ui.theme.component.AppText
import com.forknowledge.feature.nutrient.R
import com.forknowledge.feature.nutrient.StatisticsType

@Composable
fun NutrientGroupScreen(
    onNavigateToStatistics: (StatisticsType) -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            NutrientGroupTopBar(
                onNavigateBack = onNavigateBack
            )
        }
    ) { innerPadding ->
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                NutrientGroupCard(
                    nutritionType = StatisticsType.ENERGY,
                    onSelected = { onNavigateToStatistics(StatisticsType.ENERGY) }
                )

                NutrientGroupCard(
                    nutritionType = StatisticsType.CARB,
                    onSelected = { onNavigateToStatistics(StatisticsType.CARB) }
                )
            }

            Row(
                modifier = Modifier
                    .padding(top = 28.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                NutrientGroupCard(
                    nutritionType = StatisticsType.PROTEIN,
                    onSelected = { onNavigateToStatistics(StatisticsType.PROTEIN) }
                )

                NutrientGroupCard(
                    nutritionType = StatisticsType.FAT,
                    onSelected = { onNavigateToStatistics(StatisticsType.FAT) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NutrientGroupTopBar(
    onNavigateBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
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
                text = stringResource(R.string.statistics_select_nutrient_header),
                textStyle = Typography.titleSmall
            )
        }
    }
}

@Composable
fun NutrientGroupCard(
    nutritionType: StatisticsType,
    onSelected: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(100.dp)
            .clickable { onSelected() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = nutritionType.icon),
                contentDescription = null
            )
        }

        AppText(
            modifier = Modifier
                .padding(top = 4.dp)
                .fillMaxWidth(),
            text = stringResource(nutritionType.title),
            textStyle = Typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
fun SelectNutrientTopBarPreview() {
    NutrientGroupTopBar { }
}
