package com.forknowledge.feature.onboarding.ui.surveyscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.forknowledge.core.common.extension.toCm
import com.forknowledge.core.common.extension.toCompleteDouble
import com.forknowledge.core.common.extension.toFeet
import com.forknowledge.core.common.extension.toIntegerAndFraction
import com.forknowledge.core.ui.theme.Green91C747
import com.forknowledge.core.ui.theme.GreyB7BDC4
import com.forknowledge.core.ui.theme.Typography
import com.forknowledge.core.ui.theme.component.AppText
import com.forknowledge.core.ui.theme.component.WheelColumn
import com.forknowledge.feature.onboarding.R
import com.forknowledge.feature.onboarding.ui.DataUnit
import com.forknowledge.feature.onboarding.ui.fractionRange
import com.forknowledge.feature.onboarding.ui.heightInCmRange
import com.forknowledge.feature.onboarding.ui.heightInFeetRange

@Composable
fun HeightSection(
    modifier: Modifier = Modifier,
    isCmUnit: Boolean,
    height: Double,
    onUnitChanged: (Boolean) -> Unit,
    onHeightChanged: (Double) -> Unit
) {

    val intPart by remember(
        key1 = isCmUnit,
        key2 = height
    ) {
        mutableIntStateOf(
            when {
                isCmUnit && height >= DataUnit.MIN_HEIGHT_IN_CENTIMETER ||
                        !isCmUnit && height < DataUnit.MIN_HEIGHT_IN_CENTIMETER -> height.toIntegerAndFraction().first

                isCmUnit && height < DataUnit.MIN_HEIGHT_IN_CENTIMETER -> {
                    val heightInCm = height.toCm().toIntegerAndFraction()
                    heightInCm.first
                }

                !isCmUnit && height >= DataUnit.MIN_HEIGHT_IN_CENTIMETER -> {
                    val heightInFeet = height.toFeet().toIntegerAndFraction()
                    heightInFeet.first
                }

                else -> DataUnit.DEFAULT_HEIGHT_IN_FEET.toInt()
            }
        )
    }
    val fractionalPart by remember(
        key1 = isCmUnit,
        key2 = height
    ) {
        mutableIntStateOf(
            when {
                isCmUnit && height >= DataUnit.MIN_HEIGHT_IN_CENTIMETER ||
                        !isCmUnit && height < DataUnit.MIN_HEIGHT_IN_CENTIMETER -> height.toIntegerAndFraction().second

                isCmUnit && height < DataUnit.MIN_HEIGHT_IN_CENTIMETER -> {
                    val heightInCm = height.toCm().toIntegerAndFraction()
                    heightInCm.second
                }

                !isCmUnit && height >= DataUnit.MIN_HEIGHT_IN_CENTIMETER -> {
                    val heightInFeet = height.toFeet().toIntegerAndFraction()
                    heightInFeet.second
                }

                else -> (DataUnit.DEFAULT_HEIGHT_IN_FEET - DataUnit.DEFAULT_HEIGHT_IN_FEET.toInt()).toInt()
            }
        )
    }
    val itemsRange by remember(isCmUnit) {
        mutableStateOf(if (isCmUnit) heightInCmRange else heightInFeetRange)
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .width(200.dp)
                .height(50.dp)
                .background(
                    color = GreyB7BDC4,
                    shape = RoundedCornerShape(28.dp)
                ),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppText(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .background(
                        color = if (isCmUnit) {
                            Green91C747
                        } else {
                            GreyB7BDC4
                        },
                        shape = RoundedCornerShape(28.dp)
                    )
                    .clip(RoundedCornerShape(28.dp))
                    .clickable { onUnitChanged(true) },
                text = stringResource(R.string.onboarding_survey_height_unit_cm),
                textStyle = Typography.displayMedium,
                color = White,
                textAlign = TextAlign.Center
            )

            AppText(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .background(
                        color = if (isCmUnit) {
                            GreyB7BDC4
                        } else {
                            Green91C747
                        },
                        shape = RoundedCornerShape(28.dp)
                    )
                    .clip(RoundedCornerShape(28.dp))
                    .clickable { onUnitChanged(false) },
                text = stringResource(R.string.onboarding_survey_height_unit_ft),
                textStyle = Typography.displayMedium,
                color = White,
                textAlign = TextAlign.Center
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            WheelColumn(
                modifier = Modifier.width(130.dp),
                value = intPart,
                items = itemsRange.toList(),
                visibleItems = 5,
                onCenterItemChanged = {
                    onHeightChanged(
                        Pair(
                            it,
                            fractionalPart
                        ).toCompleteDouble()
                    )
                }
            )

            AppText(
                text = ".",
                textStyle = Typography.titleSmall,
                textAlign = TextAlign.Center
            )

            WheelColumn(
                modifier = Modifier.width(130.dp),
                value = fractionalPart,
                items = fractionRange.toList(),
                visibleItems = 5,
                onCenterItemChanged = {
                    onHeightChanged(
                        Pair(
                            intPart,
                            it
                        ).toCompleteDouble()
                    )
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HeightSectionPreview() {
    HeightSection(
        isCmUnit = true,
        height = 200.5,
        onUnitChanged = {},
        onHeightChanged = {}
    )
}
