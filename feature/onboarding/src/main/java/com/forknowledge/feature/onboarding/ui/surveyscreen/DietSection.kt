package com.forknowledge.feature.onboarding.ui.surveyscreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.forknowledge.core.ui.R.drawable
import com.forknowledge.core.ui.theme.Black05172C
import com.forknowledge.core.ui.theme.Green9AAD01
import com.forknowledge.core.ui.theme.GreyB7BDC4
import com.forknowledge.core.ui.theme.RedFF4950
import com.forknowledge.core.ui.theme.Typography
import com.forknowledge.core.ui.theme.YellowFFAE01
import com.forknowledge.core.ui.theme.component.AppText
import com.forknowledge.feature.onboarding.R
import com.forknowledge.core.common.healthtype.Diet
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DietSection(
    modifier: Modifier = Modifier,
    diet: Diet?,
    onDietSelected: (Diet) -> Unit
) {

    var sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showBottomSheet by remember { mutableStateOf(false) }
    val diets = Diet.entries.toList()
    var dietDetail by remember { mutableStateOf(Diet.BALANCE) }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        diets.forEach { currentDiet ->
            Row(
                modifier = Modifier
                    .selectableGroup()
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .height(70.dp)
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .clip(RoundedCornerShape(16.dp))
                    .border(
                        width = 1.dp,
                        color = GreyB7BDC4,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .selectable(
                        role = Role.RadioButton,
                        selected = (currentDiet == diet),
                        onClick = { onDietSelected(currentDiet) }
                    )
                    .padding(horizontal = 21.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (currentDiet == diet),
                    onClick = null,
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Black05172C,
                        unselectedColor = Black05172C
                    )
                )

                Column(
                    modifier = Modifier.padding(start = 16.dp),
                ) {
                    AppText(
                        text = currentDiet.dietName,
                        textStyle = Typography.labelLarge
                    )

                    AppText(
                        modifier = Modifier.padding(top = 4.dp),
                        text = currentDiet.description
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                IconButton(
                    onClick = {
                        dietDetail = currentDiet
                        showBottomSheet = true
                    }
                ) {
                    Icon(
                        painter = painterResource(drawable.ic_tooltips),
                        tint = Black,
                        contentDescription = null
                    )
                }
            }

        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = { showBottomSheet = false }
        ) {
            BottomSheetDetail(diet = dietDetail)
        }
    }
}

@Composable
fun BottomSheetDetail(diet: Diet) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        AppText(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = diet.dietName,
            textStyle = Typography.titleSmall
        )

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp)
        )

        AppText(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = stringResource(R.string.onboarding_survey_diet_macros),
            textStyle = Typography.labelLarge
        )

        Row(
            modifier = Modifier.padding(16.dp),
        ) {
            Macros(
                modifier = Modifier.weight(
                    if (diet == Diet.KETO) 0.15F else diet.macro.carbs,
                ),
                nutrition = stringResource(R.string.onboarding_survey_diet_carbs),
                ratio = (diet.macro.carbs * 100).roundToInt(),
                color = Green9AAD01
            )

            Macros(
                modifier = Modifier
                    .weight(diet.macro.protein)
                    .padding(start = 2.dp),
                nutrition = stringResource(R.string.onboarding_survey_diet_protein),
                ratio = (diet.macro.protein * 100).roundToInt(),
                color = RedFF4950
            )

            Macros(
                modifier = Modifier
                    .weight(diet.macro.fat)
                    .padding(start = 2.dp),
                nutrition = stringResource(R.string.onboarding_survey_diet_fat),
                ratio = (diet.macro.fat * 100).roundToInt(),
                color = YellowFFAE01
            )
        }

        AppText(
            modifier = Modifier.padding(
                start = 16.dp,
                end = 16.dp,
                bottom = 16.dp
            ),
            text = diet.focus
        )

        if (diet.goal.isNotEmpty()) {
            AppText(
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                ),
                text = diet.goal
            )
        }
    }
}

@Composable
fun Macros(
    modifier: Modifier = Modifier,
    nutrition: String,
    ratio: Int,
    color: Color
) {
    Column(modifier = modifier) {
        HorizontalDivider(
            modifier = Modifier
                .background(
                    color = Color.Unspecified,
                    shape = RoundedCornerShape(16.dp)
                )
                .clip(RoundedCornerShape(16.dp)),
            thickness = 4.dp,
            color = color
        )

        AppText(
            modifier = Modifier.padding(top = 8.dp),
            text = nutrition,
            textStyle = Typography.labelMedium
        )

        AppText(
            modifier = Modifier.padding(top = 4.dp),
            text = "$ratio%"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BottomSheetDetailPreview() {
    BottomSheetDetail(Diet.KETO)
}

@Preview(showBackground = true)
@Composable
fun DietSectionPreview() {
    DietSection(
        diet = null,
        onDietSelected = {}
    )
}
