package com.forknowledge.feature.nutrient.ui

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.ProgressIndicatorDefaults.drawStopIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.forknowledge.core.common.extension.nextDate
import com.forknowledge.core.common.extension.previousDate
import com.forknowledge.core.common.extension.toDayMonthDateString
import com.forknowledge.core.ui.R.drawable
import com.forknowledge.core.ui.theme.Black063336
import com.forknowledge.core.ui.theme.Black374957
import com.forknowledge.core.ui.theme.Blue05A6F1
import com.forknowledge.core.ui.theme.GreenA1CE50
import com.forknowledge.core.ui.theme.Grey8A949F
import com.forknowledge.core.ui.theme.GreyDADADA
import com.forknowledge.core.ui.theme.GreyFAFAFA
import com.forknowledge.core.ui.theme.RedFF4950
import com.forknowledge.core.ui.theme.Typography
import com.forknowledge.core.ui.theme.YellowFB880C
import com.forknowledge.core.ui.theme.component.AppText
import com.forknowledge.core.ui.theme.component.DatePickerModal
import com.forknowledge.feature.model.IntakeNutrition
import com.forknowledge.feature.model.TargetNutrition
import com.forknowledge.feature.nutrient.NutritionViewModel
import com.forknowledge.feature.nutrient.R
import java.util.Date
import kotlin.math.abs

@Composable
fun NutrientScreen(
    onNavigateToLogFood: (String) -> Unit,
    viewModel: NutritionViewModel = hiltViewModel()
) {
    val date = viewModel.date
    val targetNutrition by viewModel.targetNutrition.collectAsStateWithLifecycle()
    val intakeNutrition by viewModel.intakeNutrition.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppBarDateSelector(
                date = date,
                onDateChanged = viewModel::updateDate
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = GreyFAFAFA)
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
        ) {
            targetNutrition?.let { nutrition ->
                viewModel.getIntakeNutritionByDate()

                NutrientSection(
                    targetNutrition = nutrition,
                    intakeNutrition = intakeNutrition
                )

                MealSection(
                    targetNutrition = nutrition,
                    intakeNutrition = intakeNutrition,
                    onNavigateToLogFood = { onNavigateToLogFood(it) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBarDateSelector(
    date: Date,
    onDateChanged: (Date) -> Unit
) {
    var showDatePickerModal by remember { mutableStateOf(false) }

    if (showDatePickerModal) {
        DatePickerModal(
            headline = stringResource(R.string.nutrient_date_picker_headline),
            confirmText = stringResource(R.string.nutrient_date_picker_button_confirm_text),
            date = date.time,
            onDateSelected = { date ->
                date?.let { onDateChanged(Date(it)) }
            },
            onDismiss = { showDatePickerModal = false }
        )
    }

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = White
        ),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(
                    onClick = { onDateChanged(date.previousDate()) }
                ) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(drawable.ic_back),
                        tint = Grey8A949F,
                        contentDescription = null
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                AppText(
                    modifier = Modifier.clickable { showDatePickerModal = true },
                    text = date.toDayMonthDateString(),
                    textStyle = Typography.labelMedium
                )

                Icon(
                    modifier = Modifier
                        .size(28.dp)
                        .padding(start = 8.dp)
                        .clickable { showDatePickerModal = true },
                    painter = painterResource(drawable.ic_calendar),
                    tint = Black374957,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.weight(1f))

                IconButton(
                    onClick = { onDateChanged(date.nextDate()) }
                ) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(drawable.ic_forward),
                        tint = Grey8A949F,
                        contentDescription = null
                    )
                }
            }
        }
    )

}

@Composable
fun NutrientSection(
    targetNutrition: TargetNutrition,
    intakeNutrition: IntakeNutrition
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 24.dp,
                start = 16.dp,
                end = 16.dp
            )
            .graphicsLayer {
                shape = RoundedCornerShape(
                    topStart = 8.dp,
                    topEnd = 8.dp,
                    bottomStart = 8.dp,
                    bottomEnd = 8.dp
                )
                shadowElevation = 3.dp.toPx()
                spotShadowColor = Black063336
                clip = true
            }
            .background(
                color = White,
                shape = RoundedCornerShape(
                    topStart = 8.dp,
                    topEnd = 8.dp,
                    bottomStart = 8.dp,
                    bottomEnd = 8.dp
                )
            )
            .clip(
                RoundedCornerShape(
                    topStart = 8.dp,
                    topEnd = 8.dp
                )
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            // Eaten
            ActivityCard(
                activityLabel = stringResource(R.string.nutrient_activity_calories_label_eaten),
                activityValue = intakeNutrition.calories,
                activityUnit = stringResource(R.string.nutrient_activity_calories_unit)
            )

            NutrientProgress(
                modifier = Modifier.size(130.dp),
                isDailyCalories = true,
                progressBarWidth = 9.dp,
                progressIndicatorColor = GreenA1CE50,
                progress = intakeNutrition.calories,
                totalNutrients = targetNutrition.calories
            )

            // Burned
            ActivityCard(
                activityLabel = stringResource(R.string.nutrient_activity_calories_label_burn),
                activityValue = 0,
                activityUnit = stringResource(R.string.nutrient_activity_calories_unit)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 24.dp,
                    start = 8.dp,
                    end = 8.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppText(
                text = stringResource(R.string.nutrient_activity_calories_label_eaten),
                textStyle = Typography.labelSmall,
                color = Grey8A949F
            )

            HorizontalDivider(
                modifier = Modifier.padding(start = 16.dp),
                color = GreyDADADA
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 24.dp,
                    start = 8.dp,
                    end = 8.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Carbs
            NutrientProgress(
                modifier = Modifier.size(90.dp),
                progressBarWidth = 7.dp,
                progressIndicatorColor = RedFF4950,
                progress = intakeNutrition.carbs,
                totalNutrients = targetNutrition.carbs
            )

            // Protein
            NutrientProgress(
                modifier = Modifier.size(90.dp),
                progressBarWidth = 7.dp,
                progressIndicatorColor = YellowFB880C,
                progress = intakeNutrition.proteins,
                totalNutrients = targetNutrition.proteins
            )

            // Fat
            NutrientProgress(
                modifier = Modifier.size(90.dp),
                progressBarWidth = 7.dp,
                progressIndicatorColor = Blue05A6F1,
                progress = intakeNutrition.fats,
                totalNutrients = targetNutrition.fats
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 24.dp,
                    start = 8.dp,
                    end = 8.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppText(
                text = stringResource(R.string.nutrient_activity_calories_label_burn),
                textStyle = Typography.labelSmall,
                color = Grey8A949F
            )

            HorizontalDivider(
                modifier = Modifier.padding(start = 16.dp),
                color = GreyDADADA
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ActivityCard(
                activityLabel = stringResource(R.string.nutrient_activity_label_walking),
                activityValue = 1000,
                activityUnit = stringResource(R.string.nutrient_activity_walking_unit),
                style = Typography.titleMedium
            )

            VerticalDivider(
                modifier = Modifier.height(80.dp),
                color = GreyDADADA
            )

            ActivityCard(
                activityLabel = stringResource(R.string.nutrient_activity_label_activity),
                activityValue = 1000,
                activityUnit = stringResource(R.string.nutrient_activity_calories_unit),
                style = Typography.titleMedium
            )
        }
    }
}

@Composable
fun MealSection(
    targetNutrition: TargetNutrition,
    intakeNutrition: IntakeNutrition,
    onNavigateToLogFood: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = 24.dp,
                horizontal = 16.dp
            )
            .graphicsLayer {
                shape = RoundedCornerShape(
                    topStart = 8.dp,
                    topEnd = 8.dp,
                    bottomStart = 8.dp,
                    bottomEnd = 8.dp
                )
                shadowElevation = 3.dp.toPx()
                spotShadowColor = Black063336
                clip = true
            }
            .background(
                color = White,
                shape = RoundedCornerShape(
                    topStart = 8.dp,
                    topEnd = 8.dp,
                    bottomStart = 8.dp,
                    bottomEnd = 8.dp
                )
            )
            .clip(
                RoundedCornerShape(
                    topStart = 8.dp,
                    topEnd = 8.dp
                )
            )
    ) {
        // Breakfast
        MealCard(
            label = stringResource(R.string.nutrient_meal_label_breakfast),
            calories = intakeNutrition.breakfast.calories,
            totalCalories = targetNutrition.breakfastCalories,
            image = R.drawable.ic_breakfast,
            onNavigateToLogFood = { onNavigateToLogFood(it) }
        )

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            color = GreyDADADA
        )

        // Lunch
        MealCard(
            label = stringResource(R.string.nutrient_meal_label_lunch),
            calories = intakeNutrition.lunch.calories,
            totalCalories = targetNutrition.lunchCalories,
            image = R.drawable.ic_lunch,
            onNavigateToLogFood = { onNavigateToLogFood(it) }
        )

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            color = GreyDADADA
        )

        // Dinner
        MealCard(
            label = stringResource(R.string.nutrient_meal_label_dinner),
            calories = intakeNutrition.dinner.calories,
            totalCalories = targetNutrition.dinnerCalories,
            image = R.drawable.ic_dinner,
            onNavigateToLogFood = { onNavigateToLogFood(it) }
        )

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            color = GreyDADADA
        )

        // Snack
        MealCard(
            label = stringResource(R.string.nutrient_meal_label_snack),
            calories = intakeNutrition.snack.calories,
            totalCalories = targetNutrition.snackCalories,
            image = R.drawable.ic_snack,
            onNavigateToLogFood = { onNavigateToLogFood(it) }
        )
    }
}

@Composable
fun NutrientProgress(
    modifier: Modifier = Modifier,
    isDailyCalories: Boolean = false,
    progressBarWidth: Dp = 7.dp,
    progressIndicatorColor: Color,
    progress: Long,
    totalNutrients: Long,
) {
    var targetSweepAngle by remember { mutableFloatStateOf(0F) }
    val animatedSweepAngle by animateFloatAsState(
        targetValue = targetSweepAngle,
        animationSpec = tween(durationMillis = 800)
    )

    LaunchedEffect(progress) {
        targetSweepAngle = progress * 360F / totalNutrients
    }

    Box {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AppText(
                text = if (isDailyCalories) {
                    abs(totalNutrients - progress).toString()
                } else {
                    progress.toString()
                },
                textStyle = if (isDailyCalories) Typography.headlineSmall else Typography.titleSmall
            )

            AppText(
                text = when {
                    isDailyCalories && progress <= totalNutrients -> stringResource(R.string.nutrient_activity_calories_left)
                    isDailyCalories && progress > totalNutrients -> stringResource(R.string.nutrient_activity_calories_over)
                    else -> stringResource(R.string.nutrient_eaten_total_nutrient, totalNutrients)
                },
                textStyle = Typography.bodySmall,
                color = Grey8A949F
            )
        }

        val stroke = with(LocalDensity.current) {
            Stroke(
                width = progressBarWidth.toPx(),
                cap = StrokeCap.Round,
            )
        }

        // Under circular progress
        Canvas(
            modifier = modifier.align(Alignment.Center)
        ) {
            val innerRadius = (size.minDimension - stroke.width) / 2
            val halfSize = size / 2.0f
            val topLeft = Offset(
                halfSize.width - innerRadius,
                halfSize.height - innerRadius
            )
            val size = Size(innerRadius * 2, innerRadius * 2)

            drawArc(
                brush = Brush.radialGradient(
                    colorStops = arrayOf(
                        0.8f to Color(0xFFDADADA),
                        1f to Color(0xFFF5F5F5)
                    )
                ),
                topLeft = topLeft,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = true,
                size = size,
                style = stroke,
            )
        }

        // Circular progress
        Canvas(
            modifier = modifier.align(Alignment.Center)
        ) {
            val innerRadius = (size.minDimension - stroke.width) / 2
            val halfSize = size / 2.0f
            val topLeft = Offset(
                halfSize.width - innerRadius,
                halfSize.height - innerRadius
            )
            val size = Size(innerRadius * 2, innerRadius * 2)

            drawArc(
                color = progressIndicatorColor,
                topLeft = topLeft,
                startAngle = -90f,
                sweepAngle = animatedSweepAngle,
                useCenter = false,
                size = size,
                style = stroke
            )
        }
    }
}

@Composable
fun ActivityCard(
    activityLabel: String,
    activityValue: Long,
    activityUnit: String = stringResource(R.string.nutrient_activity_calories_unit),
    style: TextStyle = Typography.headlineSmall
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppText(
            text = activityLabel,
            textStyle = Typography.bodySmall
        )

        AppText(
            text = activityValue.toString(),
            textStyle = style
        )

        AppText(
            text = activityUnit,
            textStyle = Typography.bodySmall,
            color = Grey8A949F
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealCard(
    label: String,
    calories: Long,
    totalCalories: Long,
    @DrawableRes image: Int,
    onNavigateToLogFood: (String) -> Unit
) {
    var targetProgress by remember { mutableFloatStateOf(0F) }
    val animatedProgress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = tween(durationMillis = 800)
    )

    LaunchedEffect(calories) {
        targetProgress = calories.toFloat() / totalCalories
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = 16.dp,
                horizontal = 16.dp
            )
    ) {
        val (mealImage, icon, textLabel, textCalories, progressIndicator) = createRefs()

        Image(
            modifier = Modifier
                .size(60.dp)
                .constrainAs(mealImage) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                },
            painter = painterResource(image),
            contentDescription = null
        )

        AppText(
            modifier = Modifier.constrainAs(textLabel) {
                top.linkTo(mealImage.top, margin = 4.dp)
                start.linkTo(mealImage.end, margin = 16.dp)
            },
            text = label,
            textStyle = Typography.labelLarge
        )

        LinearProgressIndicator(
            modifier = Modifier
                .width(60.dp)
                .height(6.dp)
                .constrainAs(progressIndicator) {
                    top.linkTo(textCalories.top)
                    bottom.linkTo(textCalories.bottom)
                    start.linkTo(mealImage.end, margin = 16.dp)
                },
            color = GreenA1CE50,
            trackColor = GreyDADADA,
            progress = { animatedProgress },
            gapSize = (-4).dp,
            drawStopIndicator = {
                drawStopIndicator(
                    drawScope = this,
                    stopSize = ProgressIndicatorDefaults.LinearTrackStopIndicatorSize,
                    color = if (calories >= totalCalories) GreenA1CE50 else GreyDADADA,
                    strokeCap = StrokeCap.Round
                )
            }
        )

        AppText(
            modifier = Modifier.constrainAs(textCalories) {
                bottom.linkTo(mealImage.bottom, margin = 4.dp)
                start.linkTo(progressIndicator.end, margin = 16.dp)
            },
            text = stringResource(
                R.string.nutrient_meal_progress_calories,
                calories,
                totalCalories
            ),
            textStyle = Typography.bodySmall,
            color = Grey8A949F
        )

        IconButton(
            modifier = Modifier
                .constrainAs(icon) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end, margin = 16.dp)
                },
            onClick = { onNavigateToLogFood(label) }
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_add_solid),
                tint = Black374957,
                contentDescription = null
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun DateSelectorPreview() {
    AppBarDateSelector(Date()) {}
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun NutrientSectionPreview() {
    NutrientSection(
        targetNutrition = TargetNutrition(
            calories = 2000,
            carbs = 200,
            proteins = 200,
            fats = 200
        ),
        intakeNutrition = IntakeNutrition(
            calories = 1500,
            carbs = 150,
            proteins = 150,
            fats = 150
        )
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun MealSectionPreview() {
    MealSection(
        targetNutrition = TargetNutrition(
            calories = 2000,
            carbs = 200,
            proteins = 200,
            fats = 200
        ),
        intakeNutrition = IntakeNutrition(
            calories = 1500,
            carbs = 150,
            proteins = 150,
            fats = 150
        ),
        onNavigateToLogFood = {}
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun NutrientProgressPreview() {
    NutrientProgress(
        modifier = Modifier.size(90.dp),
        progressBarWidth = 7.dp,
        progressIndicatorColor = GreenA1CE50,
        progress = 158,
        totalNutrients = 224
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun ActivityCardPreview() {
    ActivityCard(
        activityLabel = "Eaten",
        activityValue = 1500,
        activityUnit = "kcal"
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun MealCardPreview() {
    MealCard(
        label = "Breakfast",
        calories = 1500,
        totalCalories = 2000,
        image = R.drawable.ic_snack,
        onNavigateToLogFood = {}
    )
}
