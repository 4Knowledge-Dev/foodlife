package com.forknowledge.feature.onboarding.ui.surveyscreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.forknowledge.core.ui.theme.Black05172C
import com.forknowledge.core.ui.theme.GreyB7BDC4
import com.forknowledge.core.ui.theme.Typography
import com.forknowledge.core.ui.theme.component.AppText
import com.forknowledge.core.common.healthtype.ActivityLevel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ActivityLevelSection(
    modifier: Modifier = Modifier,
    level: ActivityLevel?,
    onActivityLevelSelected: (ActivityLevel) -> Unit
) {

    val levels = ActivityLevel.entries.toList()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center
    ) {
        levels.forEach { currentLevel ->
            Row(
                modifier = Modifier
                    .selectableGroup()
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .height(90.dp)
                    .background(
                        color = Color.White, shape = RoundedCornerShape(12.dp)
                    )
                    .clip(RoundedCornerShape(16.dp))
                    .border(
                        width = 1.dp, color = GreyB7BDC4, shape = RoundedCornerShape(16.dp)
                    )
                    .selectable(
                        role = Role.RadioButton,
                        selected = (currentLevel == level),
                        onClick = { onActivityLevelSelected(currentLevel) })
                    .padding(horizontal = 21.dp), verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = (currentLevel == level),
                    onClick = null,
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Black05172C, unselectedColor = Black05172C
                    )
                )

                Column(
                    modifier = Modifier.padding(start = 16.dp),
                ) {
                    AppText(
                        text = currentLevel.level, textStyle = Typography.labelLarge
                    )

                    AppText(
                        modifier = Modifier.padding(top = 4.dp),
                        text = currentLevel.description,
                        textStyle = Typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ActivityLevelSectionPreview() {
    ActivityLevelSection(
        level = null, onActivityLevelSelected = {})
}
