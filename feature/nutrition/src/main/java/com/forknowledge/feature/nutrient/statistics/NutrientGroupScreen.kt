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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.forknowledge.core.common.AppConstant.NUTRIENT_CALORIES_NAME
import com.forknowledge.core.common.AppConstant.NUTRIENT_CARB_NAME
import com.forknowledge.core.common.AppConstant.NUTRIENT_FAT_NAME
import com.forknowledge.core.common.AppConstant.NUTRIENT_MINERALS_NAME
import com.forknowledge.core.common.AppConstant.NUTRIENT_OTHER_NUTRIENT_NAME
import com.forknowledge.core.common.AppConstant.NUTRIENT_PROTEIN_NAME
import com.forknowledge.core.common.AppConstant.NUTRIENT_VITAMINS_NAME
import com.forknowledge.core.ui.theme.GreyF4F5F5
import com.forknowledge.core.ui.theme.Typography
import com.forknowledge.core.ui.theme.component.AppText
import com.forknowledge.feature.nutrient.NutritionGroupType
import com.forknowledge.feature.nutrient.R
import com.forknowledge.feature.nutrient.statistics.component.NutrientGroupTopBar

@Composable
fun NutrientGroupScreen(
    onNavigateToAdditionalNutrition: (
        nutritionName: String,
        nutritionType: NutritionGroupType
    ) -> Unit,
    onNavigateToStatistics: (
        nutritionName: String,
        nutritionType: NutritionGroupType
    ) -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            NutrientGroupTopBar(
                title = stringResource(R.string.statistics_select_nutrient_header),
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
                .padding(vertical = 24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                NutrientGroupCard(
                    groupName = stringResource(R.string.statistics_nutrition_group_energy),
                    nutritionType = NutritionGroupType.ENERGY,
                    onSelected = {
                        onNavigateToStatistics(
                            NUTRIENT_CALORIES_NAME,
                            NutritionGroupType.ENERGY
                        )
                    }
                )

                NutrientGroupCard(
                    groupName = stringResource(R.string.statistics_nutrition_group_carbs),
                    nutritionType = NutritionGroupType.CARB,
                    onSelected = {
                        onNavigateToStatistics(
                            NUTRIENT_CARB_NAME,
                            NutritionGroupType.CARB
                        )
                    }
                )
            }

            Row(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                NutrientGroupCard(
                    groupName = stringResource(R.string.nutrient_label_protein),
                    nutritionType = NutritionGroupType.PROTEIN,
                    onSelected = {
                        onNavigateToStatistics(
                            NUTRIENT_PROTEIN_NAME,
                            NutritionGroupType.PROTEIN
                        )
                    }
                )

                NutrientGroupCard(
                    groupName = stringResource(R.string.nutrient_label_fat),
                    nutritionType = NutritionGroupType.FAT,
                    onSelected = {
                        onNavigateToStatistics(
                            NUTRIENT_FAT_NAME,
                            NutritionGroupType.FAT
                        )
                    }
                )
            }

            Row(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                NutrientGroupCard(
                    groupName = stringResource(R.string.statistics_nutrition_group_other_nutrition),
                    nutritionType = NutritionGroupType.OTHER_NUTRIENT,
                    onSelected = {
                        onNavigateToAdditionalNutrition(
                            NUTRIENT_OTHER_NUTRIENT_NAME,
                            NutritionGroupType.OTHER_NUTRIENT
                        )
                    }
                )

                NutrientGroupCard(
                    groupName = stringResource(R.string.statistics_nutrition_group_vitamins),
                    nutritionType = NutritionGroupType.VITAMINS,
                    onSelected = {
                        onNavigateToAdditionalNutrition(
                            NUTRIENT_VITAMINS_NAME,
                            NutritionGroupType.VITAMINS
                        )
                    }
                )
            }

            Row(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                NutrientGroupCard(
                    groupName = stringResource(R.string.statistics_nutrition_group_minerals),
                    nutritionType = NutritionGroupType.MINERALS,
                    onSelected = {
                        onNavigateToAdditionalNutrition(
                            NUTRIENT_MINERALS_NAME,
                            NutritionGroupType.MINERALS
                        )
                    }
                )

                NutrientGroupCard(
                    groupName = stringResource(R.string.statistics_nutrition_group_other),
                    nutritionType = NutritionGroupType.ACTIVITY,
                    onSelected = {
                        onNavigateToAdditionalNutrition(
                            NUTRIENT_MINERALS_NAME,
                            NutritionGroupType.ACTIVITY
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun NutrientGroupCard(
    groupName: String,
    nutritionType: NutritionGroupType,
    onSelected: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(140.dp)
            .background(
                color = GreyF4F5F5,
                shape = RoundedCornerShape(12.dp)
            )
            .clip(RoundedCornerShape(12.dp))
            .clickable { onSelected() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
                .background(nutritionType.color),
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = Modifier.size(80.dp),
                painter = painterResource(id = nutritionType.icon),
                contentDescription = null
            )
        }

        AppText(
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 4.dp)
                .fillMaxWidth(),
            text = groupName,
            textStyle = Typography.labelLarge,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview(backgroundColor = 0xFFFFFFFF, showBackground = true)
@Composable
fun NutrientGroupCardPreview() {
    NutrientGroupCard(
        groupName = stringResource(R.string.statistics_nutrition_group_energy),
        nutritionType = NutritionGroupType.ENERGY,
        onSelected = {}
    )
}

@Preview
@Composable
fun NutrientGroupScreenPreview() {
    NutrientGroupScreen(
        onNavigateToAdditionalNutrition = { _, _ -> },
        onNavigateToStatistics = { _, _ -> },
        onNavigateBack = {}
    )
}
