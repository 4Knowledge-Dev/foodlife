package com.forknowledge.feature.onboarding.ui.surveyscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.forknowledge.core.ui.theme.Black05172C
import com.forknowledge.core.ui.theme.Typography
import com.forknowledge.feature.onboarding.R
import com.forknowledge.core.common.healthtype.Gender

@Composable
fun GenderSection(
    gender: Gender?,
    onGenderSelected: (gender: Gender) -> Unit
) {
    val genders = Gender.entries.toList()

    Row(
        modifier = Modifier
            .selectableGroup()
            .fillMaxWidth()
            .padding(top = 80.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        genders.forEach { currentGender ->
            Box(
                modifier = Modifier
                    .shadow(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(24.dp)
                    )
                    .size(150.dp)
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(24.dp)
                    )
                    .selectable(
                        selected = (currentGender == gender),
                        onClick = { onGenderSelected(currentGender) },
                        role = Role.RadioButton
                    )
            ) {
                RadioButton(
                    modifier = Modifier.padding(
                        top = 8.dp,
                        start = 8.dp,
                    ),
                    selected = (currentGender == gender),
                    onClick = null,
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Black05172C,
                        unselectedColor = Black05172C
                    )
                )
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        modifier = Modifier
                            .size(40.dp)
                            .drawBehind {
                                drawCircle(
                                    color = Color.White,
                                    radius = 100f
                                )
                            },
                        painter = if (currentGender.isMale) {
                            painterResource(id = R.drawable.img_male)
                        } else {
                            painterResource(id = R.drawable.img_female)
                        },
                        contentDescription = "Gender"
                    )

                    Text(
                        modifier = Modifier.padding(top = 16.dp),
                        text = if (currentGender.isMale) {
                            stringResource(R.string.onboarding_survey_gender_male)
                        } else {
                            stringResource(R.string.onboarding_survey_gender_female)
                        },
                        style = Typography.titleSmall
                    )
                }
            }

            if (currentGender.isMale) {
                Spacer(modifier = Modifier.padding(end = 20.dp))
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF4F4F4)
@Composable
fun GenderPreview() {
    GenderSection(gender = null, onGenderSelected = {})
}
