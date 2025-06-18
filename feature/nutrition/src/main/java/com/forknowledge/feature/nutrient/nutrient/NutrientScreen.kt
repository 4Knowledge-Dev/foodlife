package com.forknowledge.feature.nutrient.nutrient

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.ProgressIndicatorDefaults.drawStopIndicator
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.forknowledge.core.common.AppConstant.RECIPE_NUTRIENT_CALORIES_INDEX
import com.forknowledge.core.common.AppConstant.RECIPE_NUTRIENT_CARB_INDEX
import com.forknowledge.core.common.AppConstant.RECIPE_NUTRIENT_FAT_INDEX
import com.forknowledge.core.common.AppConstant.RECIPE_NUTRIENT_PROTEIN_INDEX
import com.forknowledge.core.common.caloriesToNutrientAmount
import com.forknowledge.core.common.extension.nextDate
import com.forknowledge.core.common.extension.previousDate
import com.forknowledge.core.common.extension.toDayMonthDateString
import com.forknowledge.core.common.extension.toFirestoreDateTime
import com.forknowledge.core.common.getCurrentDateTime
import com.forknowledge.core.common.healthtype.NutrientType
import com.forknowledge.core.data.model.NutritionDisplayData
import com.forknowledge.core.ui.R.drawable
import com.forknowledge.core.ui.theme.Black063336
import com.forknowledge.core.ui.theme.Black374957
import com.forknowledge.core.ui.theme.Blue05A6F1
import com.forknowledge.core.ui.theme.Green91C747
import com.forknowledge.core.ui.theme.GreenA1CE50
import com.forknowledge.core.ui.theme.Grey8A949F
import com.forknowledge.core.ui.theme.GreyDADADA
import com.forknowledge.core.ui.theme.GreyFAFAFA
import com.forknowledge.core.ui.theme.RedFF3939
import com.forknowledge.core.ui.theme.RedFF4950
import com.forknowledge.core.ui.theme.Typography
import com.forknowledge.core.ui.theme.YellowFB880C
import com.forknowledge.core.ui.theme.component.AppText
import com.forknowledge.core.ui.theme.component.DatePickerModal
import com.forknowledge.feature.model.userdata.NutrientData
import com.forknowledge.feature.model.userdata.TargetNutrition
import com.forknowledge.feature.nutrient.R
import java.util.Date
import kotlin.math.abs
import kotlin.math.roundToLong

@Composable
fun NutrientScreen(
    onNavigateToLogFood: (Int, Long) -> Unit,
    onNavigateToInsights: (Long) -> Unit,
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

                AppText(
                    modifier = Modifier
                        .padding(
                            top = 24.dp,
                            start = 16.dp,
                            end = 16.dp
                        )
                        .fillMaxWidth()
                        .clickable { onNavigateToInsights(date.time) },
                    text = stringResource(R.string.nutrient_label_detail),
                    textStyle = Typography.labelLarge,
                    textAlign = TextAlign.End,
                    color = Green91C747
                )

                NutrientSection(
                    targetNutrition = nutrition,
                    intakeNutrition = intakeNutrition.nutrients
                )

                MealSection(
                    targetNutrition = nutrition,
                    intakeNutrition = intakeNutrition,
                    onNavigateToLogFood = {
                        onNavigateToLogFood(it, date.time)
                    }
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
                date?.let { onDateChanged(it.toFirestoreDateTime()) }
            },
            onDismiss = { showDatePickerModal = false }
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(
                horizontal = 8.dp,
                vertical = 4.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = { onDateChanged(date.previousDate()) }
        ) {
            Icon(
                painter = painterResource(drawable.ic_back),
                tint = Grey8A949F,
                contentDescription = null
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        AppText(
            modifier = Modifier.clickable { showDatePickerModal = true },
            text = date.toDayMonthDateString(),
            textStyle = MaterialTheme.typography.labelLarge
        )

        Icon(
            modifier = Modifier
                .padding(start = 12.dp)
                .clickable { onDateChanged(getCurrentDateTime()) },
            painter = painterResource(drawable.ic_calendar),
            tint = Black374957,
            contentDescription = null
        )

        Spacer(modifier = Modifier.weight(1f))

        IconButton(
            onClick = { onDateChanged(date.nextDate()) }
        ) {
            Icon(
                painter = painterResource(drawable.ic_forward),
                tint = Grey8A949F,
                contentDescription = null
            )
        }
    }
}

@Composable
fun NutrientSection(
    targetNutrition: TargetNutrition,
    intakeNutrition: List<NutrientData>?
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 16.dp,
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
            val intakeCalories =
                intakeNutrition?.get(RECIPE_NUTRIENT_CALORIES_INDEX)?.amount?.roundToLong() ?: 0

            // Eaten
            ActivityCard(
                activityLabel = stringResource(R.string.nutrient_activity_calories_label_eaten),
                activityValue = intakeCalories,
                activityUnit = stringResource(R.string.nutrient_activity_calories_unit)
            )

            NutrientProgress(
                modifier = Modifier.size(130.dp),
                isDailyCalories = true,
                progressBarWidth = 9.dp,
                progressIndicatorColor = if (intakeCalories <= targetNutrition.calories) {
                    GreenA1CE50
                } else {
                    RedFF3939
                },
                progress = intakeCalories,
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
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                NutrientProgress(
                    modifier = Modifier.size(90.dp),
                    progressBarWidth = 7.dp,
                    progressIndicatorColor = RedFF4950,
                    progress = intakeNutrition?.get(RECIPE_NUTRIENT_CARB_INDEX)?.amount?.roundToLong()
                        ?: 0,
                    totalNutrients = caloriesToNutrientAmount(
                        nutrient = NutrientType.CARBOHYDRATE,
                        calories = (targetNutrition.calories * targetNutrition.carbRatio)
                    )
                )

                AppText(
                    modifier = Modifier.padding(top = 8.dp),
                    text = stringResource(R.string.nutrient_label_carb),
                    textStyle = Typography.bodyMedium
                )
            }

            // Protein
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                NutrientProgress(
                    modifier = Modifier.size(90.dp),
                    progressBarWidth = 7.dp,
                    progressIndicatorColor = YellowFB880C,
                    progress = intakeNutrition?.get(RECIPE_NUTRIENT_PROTEIN_INDEX)?.amount?.roundToLong()
                        ?: 0,
                    totalNutrients = caloriesToNutrientAmount(
                        nutrient = NutrientType.PROTEIN,
                        calories = (targetNutrition.calories * targetNutrition.proteinRatio)
                    )
                )

                AppText(
                    modifier = Modifier.padding(top = 8.dp),
                    text = stringResource(R.string.nutrient_label_protein),
                    textStyle = Typography.bodyMedium
                )
            }

            // Fat
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                NutrientProgress(
                    modifier = Modifier.size(90.dp),
                    progressBarWidth = 7.dp,
                    progressIndicatorColor = Blue05A6F1,
                    progress = intakeNutrition?.get(RECIPE_NUTRIENT_FAT_INDEX)?.amount?.roundToLong()
                        ?: 0,
                    totalNutrients = caloriesToNutrientAmount(
                        nutrient = NutrientType.CARBOHYDRATE,
                        calories = (targetNutrition.calories * targetNutrition.fatRatio)
                    )
                )

                AppText(
                    modifier = Modifier.padding(top = 8.dp),
                    text = stringResource(R.string.nutrient_label_fat),
                    textStyle = Typography.bodyMedium
                )
            }
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
    intakeNutrition: NutritionDisplayData,
    onNavigateToLogFood: (Int) -> Unit
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
            calories = intakeNutrition.mealCalories[0],
            totalCalories = (targetNutrition.calories * targetNutrition.breakfastRatio).roundToLong(),
            image = drawable.ic_breakfast,
            onNavigateToLogFood = { onNavigateToLogFood(1) }
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
            calories = intakeNutrition.mealCalories[1],
            totalCalories = (targetNutrition.calories * targetNutrition.lunchRatio).roundToLong(),
            image = drawable.ic_lunch,
            onNavigateToLogFood = { onNavigateToLogFood(2) }
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
            calories = intakeNutrition.mealCalories[2],
            totalCalories = (targetNutrition.calories * targetNutrition.dinnerRatio).roundToLong(),
            image = drawable.ic_dinner,
            onNavigateToLogFood = { onNavigateToLogFood(3) }
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
            calories = intakeNutrition.mealCalories[3],
            totalCalories = (targetNutrition.calories * targetNutrition.snacksRatio).roundToLong(),
            image = drawable.ic_snack,
            onNavigateToLogFood = { onNavigateToLogFood(4) }
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
        animationSpec = tween(durationMillis = 1000)
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
                painter = painterResource(drawable.ic_add_solid),
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
            carbRatio = 0.5,
            proteinRatio = 0.3,
            fatRatio = 0.2
        ),
        intakeNutrition = NutritionDisplayData().nutrients
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun MealSectionPreview() {
    MealSection(
        targetNutrition = TargetNutrition(
            calories = 2000,
            carbRatio = 50.0,
            proteinRatio = 30.0,
            fatRatio = 20.0
        ),
        intakeNutrition = NutritionDisplayData(),
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
        image = drawable.ic_snack,
        onNavigateToLogFood = {}
    )
}
