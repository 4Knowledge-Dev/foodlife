package com.forknowledge.feature.nutrient.statistics

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.forknowledge.core.ui.theme.Typography
import com.forknowledge.core.ui.theme.component.AppText
import com.forknowledge.feature.nutrient.NutritionGroupType
import com.forknowledge.feature.nutrient.minerals
import com.forknowledge.feature.nutrient.otherNutrients
import com.forknowledge.feature.nutrient.statistics.component.NutrientGroupTopBar
import com.forknowledge.feature.nutrient.vitamins

@Composable
fun AdditionalNutritionScreen(
    groupName: String,
    nutritionType: NutritionGroupType,
    onNavigateToStatistics: (
        nutritionName: String,
        nutritionType: NutritionGroupType
    ) -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            NutrientGroupTopBar(
                title = groupName,
                onNavigateBack = onNavigateBack
            )
        }
    ) { innerPadding ->
        val nutritionList = when (nutritionType) {
            NutritionGroupType.OTHER_NUTRIENT -> otherNutrients
            NutritionGroupType.VITAMINS -> vitamins
            NutritionGroupType.MINERALS -> minerals
            else -> emptyList()
        }

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(vertical = 16.dp)
        ) {
            nutritionList.forEach { nutrition ->
                AppText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigateToStatistics(nutrition, nutritionType) }
                        .padding(
                            vertical = 16.dp,
                            horizontal = 24.dp
                        ),
                    text = nutrition,
                    textStyle = Typography.displaySmall
                )
            }
        }
    }
}

@Preview
@Composable
fun AdditionalNutritionScreenPreview() {
    AdditionalNutritionScreen(
        groupName = "Other Nutrients",
        nutritionType = NutritionGroupType.OTHER_NUTRIENT,
        onNavigateToStatistics = { _, _ -> },
        onNavigateBack = {}
    )
}
