package com.forknowledge.feature.onboarding.ui.surveyscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.forknowledge.core.common.extension.toCompleteDouble
import com.forknowledge.core.common.extension.toIntegerAndFraction
import com.forknowledge.core.common.extension.toKilograms
import com.forknowledge.core.common.extension.toPounds
import com.forknowledge.core.ui.theme.Green91C747
import com.forknowledge.core.ui.theme.GreyB7BDC4
import com.forknowledge.core.ui.theme.RedFF4950
import com.forknowledge.core.ui.theme.Typography
import com.forknowledge.core.ui.theme.component.AppText
import com.forknowledge.core.ui.theme.component.WheelColumn
import com.forknowledge.feature.onboarding.R
import com.forknowledge.feature.onboarding.ui.fractionRange
import com.forknowledge.core.common.healthtype.TargetWeightError
import com.forknowledge.feature.onboarding.ui.weightInKgRange
import com.forknowledge.feature.onboarding.ui.weightInLbRange

@Composable
fun WeightSection(
    targetWeightError: TargetWeightError? = null,
    weight: Double,
    isKgUnit: Boolean,
    onWeightChanged: (Double) -> Unit,
    onUnitChanged: (Boolean) -> Unit
) {

    var isUnitUpdated = false

    val intPart by remember(
        key1 = isKgUnit,
        key2 = weight
    ) {
        mutableIntStateOf(
            when {
                isUnitUpdated && isKgUnit -> {
                    val weightInKilogram = weight.toKilograms().toIntegerAndFraction()
                    weightInKilogram.first
                }

                isUnitUpdated && !isKgUnit -> {
                    val weightInPound = weight.toPounds().toIntegerAndFraction()
                    weightInPound.first
                }

                else -> weight.toIntegerAndFraction().first
            }
        )
    }
    val fractionalPart by remember(
        key1 = isKgUnit,
        key2 = weight
    ) {
        mutableIntStateOf(
            when {
                isUnitUpdated && isKgUnit -> {
                    val weightInKilogram = weight.toKilograms().toIntegerAndFraction()
                    weightInKilogram.second
                }

                isUnitUpdated && !isKgUnit -> {
                    val weightInPound = weight.toPounds().toIntegerAndFraction()
                    weightInPound.second
                }

                else -> weight.toIntegerAndFraction().second
            }
        )
    }
    val itemsRange by remember(isKgUnit) {
        mutableStateOf(if (isKgUnit) weightInKgRange else weightInLbRange)
    }

    Box {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
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
                            color = if (isKgUnit) {
                                Green91C747
                            } else {
                                GreyB7BDC4
                            },
                            shape = RoundedCornerShape(28.dp)
                        )
                        .clip(RoundedCornerShape(28.dp))
                        .clickable {
                            isUnitUpdated = true
                            onUnitChanged(true)
                        },
                    text = stringResource(R.string.onboarding_survey_weight_unit_kg),
                    textStyle = Typography.displayMedium,
                    color = White,
                    textAlign = TextAlign.Center
                )

                AppText(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .background(
                            color = if (isKgUnit) {
                                GreyB7BDC4
                            } else {
                                Green91C747
                            },
                            shape = RoundedCornerShape(28.dp)
                        )
                        .clip(RoundedCornerShape(28.dp))
                        .clickable {
                            isUnitUpdated = true
                            onUnitChanged(false)
                        },
                    text = stringResource(R.string.onboarding_survey_weight_unit_lb),
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
                    visibleItems = 3,
                    onCenterItemChanged = {
                        isUnitUpdated = false
                        onWeightChanged(
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
                    visibleItems = 3,
                    onCenterItemChanged = {
                        isUnitUpdated = false
                        onWeightChanged(
                            Pair(
                                intPart,
                                it
                            ).toCompleteDouble()
                        )
                    }
                )
            }
        }

        Row(
            modifier = Modifier.align(Alignment.BottomCenter),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_error),
                contentDescription = null,
                tint = RedFF4950
            )

            AppText(
                modifier = Modifier.padding(start = 8.dp),
                text = targetWeightError?.error ?: "",
                textStyle = Typography.bodyLarge,
                color = RedFF4950
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CurrentWeightSectionPreview() {
    WeightSection(
        targetWeightError = TargetWeightError.GREATER,
        weight = 200.5,
        isKgUnit = true,
        onWeightChanged = {},
        onUnitChanged = {}
    )
}
