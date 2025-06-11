package com.forknowledge.feature.planner

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.forknowledge.core.common.extension.toDayAndDateString
import com.forknowledge.core.ui.R.drawable
import com.forknowledge.core.ui.theme.Black063336
import com.forknowledge.core.ui.theme.Black374957
import com.forknowledge.core.ui.theme.Green86BF3E
import com.forknowledge.core.ui.theme.GreenA1CE50
import com.forknowledge.core.ui.theme.Grey808993
import com.forknowledge.core.ui.theme.GreyEBEBEB
import com.forknowledge.core.ui.theme.Typography
import com.forknowledge.core.ui.theme.component.AppText
import java.time.LocalDate

@Composable
fun PlannerScreen() {
    val weekDays = getCurrentWeekDays()
    var selectedTab by remember {
        mutableIntStateOf(weekDays.indexOf(getCurrentDate()))
    }

    Scaffold(
        topBar = {
            Column {
                MealPlannerTopBar(
                    selectedTabIndex = selectedTab,
                    weekDays = weekDays,
                    onDateSelected = { selectedTab = it }
                )

                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 2.dp,
                    color = GreyEBEBEB
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(White)
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
        ) {
            repeat(3) {
                MealSection("Lunch")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealPlannerTopBar(
    selectedTabIndex: Int,
    weekDays: List<LocalDate>,
    onDateSelected: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(top = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        weekDays.forEachIndexed { index, day ->
            val dayOfTheWeek = day.toDayAndDateString()
            Column(
                Modifier
                    .size(
                        width = 40.dp,
                        height = 60.dp
                    )
                    .clickable { onDateSelected(index) },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AppText(
                    text = dayOfTheWeek.slice(0..1),
                    textStyle = Typography.bodyMedium
                )

                AppText(
                    text = dayOfTheWeek.substring(3),
                    textStyle = Typography.labelMedium
                )

                Spacer(modifier = Modifier.weight(1f))

                if (index == selectedTabIndex) {
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = 2.dp,
                        color = GreenA1CE50
                    )
                }
            }
        }
    }
}

@Composable
fun MealSection(
    meal: String,
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        val (textMeal, textCalories, actionIcon, mealList) = createRefs()

        AppText(
            modifier = Modifier.constrainAs(textMeal) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
            },
            text = meal,
            textStyle = Typography.titleSmall
        )

        AppText(
            modifier = Modifier.constrainAs(textCalories) {
                top.linkTo(textMeal.bottom, margin = 12.dp)
                start.linkTo(parent.start)
            },
            text = "300 kcal",
            textStyle = Typography.labelLarge,
            color = Green86BF3E
        )

        Icon(
            modifier = Modifier
                .size(28.dp)
                .constrainAs(actionIcon) {
                    top.linkTo(textMeal.top)
                    bottom.linkTo(textCalories.bottom)
                    end.linkTo(parent.end)
                },
            painter = painterResource(drawable.ic_add_solid),
            tint = Black374957,
            contentDescription = null
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(mealList) {
                    top.linkTo(textCalories.bottom, margin = 12.dp)
                    bottom.linkTo(parent.bottom)
                }
        ) {
            repeat(3) {
                MealItem()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealItem() {
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    if (showBottomSheet) {
        ModalBottomSheet(
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.background,
            onDismissRequest = { showBottomSheet = false }
        ) {
            ActionBottomSheet()
        }
    }

    ConstraintLayout(
        modifier = Modifier
            .padding(top = 12.dp)
            .fillMaxWidth()
            .graphicsLayer {
                shape = RoundedCornerShape(8.dp)
                shadowElevation = 3.dp.toPx()
                spotShadowColor = Black063336
                clip = true
            }
            .background(
                color = White,
                shape = RoundedCornerShape(8.dp)
            )
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.background)
            .clickable {}

    ) {
        val (recipeImage, recipeName, recipeInfo, actionIcon) = createRefs()

        Image(
            modifier = Modifier
                .size(100.dp)
                .background(
                    color = Color.Unspecified,
                    shape = RoundedCornerShape(8.dp)
                )
                .clip(RoundedCornerShape(8.dp))
                .constrainAs(recipeImage) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                },
            painter = painterResource(drawable.img_sample),
            contentScale = ContentScale.Crop,
            contentDescription = null
        )

        AppText(
            modifier = Modifier
                .padding(top = 4.dp)
                .widthIn(max = 210.dp)
                .constrainAs(recipeName) {
                    top.linkTo(recipeImage.top, margin = 4.dp)
                    start.linkTo(recipeImage.end, margin = 16.dp)
                    end.linkTo(parent.end)
                    horizontalBias = 0F
                },
            text = "Spaghetti Bolognese ".repeat(3),
            textStyle = Typography.labelMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        AppText(
            modifier = Modifier
                .widthIn(max = 230.dp)
                .constrainAs(recipeInfo) {
                    top.linkTo(recipeName.bottom)
                    bottom.linkTo(recipeImage.bottom, margin = 4.dp)
                    start.linkTo(recipeImage.end, margin = 16.dp)
                    end.linkTo(actionIcon.start)
                    horizontalBias = 0F
                },
            text = "23 min",
            textStyle = Typography.bodySmall,
            color = Grey808993
        )

        Icon(
            modifier = Modifier
                .size(28.dp)
                .clickable { showBottomSheet = true }
                .constrainAs(actionIcon) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end, margin = 8.dp)
                },
            painter = painterResource(R.drawable.ic_options),
            tint = Black374957,
            contentDescription = null
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionBottomSheet() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MealAction.entries.forEach { action ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
                    .clickable {}
                    .padding(
                        horizontal = 32.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(action.icon),
                    tint = Black374957,
                    contentDescription = null
                )

                AppText(
                    modifier = Modifier.padding(start = 32.dp),
                    text = stringResource(action.label),
                    textStyle = Typography.titleSmall
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ActionBottomSheetPreview() {
    ActionBottomSheet()
}

@Preview()
@Composable
fun MealPlannerTopBarPreview() {
    MealPlannerTopBar(
        selectedTabIndex = 0,
        weekDays = getCurrentWeekDays(),
        onDateSelected = {}
    )
}

@Preview()
@Composable
fun MealItemPreview() {
    MealItem()
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun MealSectionPreview() {
    MealSection(meal = "Breakfast")
}
