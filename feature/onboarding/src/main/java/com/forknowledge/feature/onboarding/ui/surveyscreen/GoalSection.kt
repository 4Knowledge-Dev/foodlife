package com.forknowledge.feature.onboarding.ui.surveyscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.forknowledge.core.common.healthtype.Goal

@Composable
fun GoalSection(
    modifier: Modifier = Modifier,
    goal: Goal?,
    onGoalSelected: (Goal) -> Unit
) {
    val goals = Goal.entries.toList()

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        goals.forEach { currentGoal ->
            Row(
                modifier = Modifier
                    .selectableGroup()
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .height(70.dp)
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clip(RoundedCornerShape(16.dp))
                    .border(
                        width = 1.dp,
                        color = GreyB7BDC4,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(horizontal = 21.dp)
                    .selectable(
                        role = Role.RadioButton,
                        selected = (currentGoal == goal),
                        onClick = { onGoalSelected(currentGoal) }
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (currentGoal == goal),
                    onClick = null,
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Black05172C,
                        unselectedColor = Black05172C
                    )
                )

                AppText(
                    modifier = Modifier.padding(start = 16.dp),
                    text = currentGoal.goalName,
                    textStyle = Typography.labelLarge
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF4F4F4)
@Composable
fun GoalSectionPreview() {
    GoalSection(goal = null, onGoalSelected = {})
}
