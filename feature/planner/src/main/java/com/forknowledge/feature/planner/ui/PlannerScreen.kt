package com.forknowledge.feature.planner.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.forknowledge.core.common.ResultState
import com.forknowledge.core.common.extension.toDayAndDateString
import com.forknowledge.core.common.extension.toEpochMillis
import com.forknowledge.core.common.extension.toEpochSeconds
import com.forknowledge.core.common.getCurrentDate
import com.forknowledge.core.common.getCurrentWeekDays
import com.forknowledge.core.common.getGreetingText
import com.forknowledge.core.ui.R.drawable
import com.forknowledge.core.ui.theme.Black374957
import com.forknowledge.core.ui.theme.Blue05A6F1
import com.forknowledge.core.ui.theme.Green86BF3E
import com.forknowledge.core.ui.theme.Green91C747
import com.forknowledge.core.ui.theme.GreenA1CE50
import com.forknowledge.core.ui.theme.Grey7F000000
import com.forknowledge.core.ui.theme.Grey808993
import com.forknowledge.core.ui.theme.GreyDADADA
import com.forknowledge.core.ui.theme.GreyEBEBEB
import com.forknowledge.core.ui.theme.OrangeFB880C
import com.forknowledge.core.ui.theme.RedFF4950
import com.forknowledge.core.ui.theme.Typography
import com.forknowledge.core.ui.theme.component.AppFloatingButton
import com.forknowledge.core.ui.theme.component.AppSnackBar
import com.forknowledge.core.ui.theme.component.AppText
import com.forknowledge.core.ui.theme.component.LoadingIndicator
import com.forknowledge.core.ui.theme.state.FloatingAction
import com.forknowledge.core.ui.theme.state.SnackBarState
import com.forknowledge.feature.model.MealRecipe
import com.forknowledge.feature.planner.R
import com.forknowledge.feature.planner.SheetAction
import java.time.LocalDate

@Composable
fun PlannerScreen(
    viewModel: MealPlannerViewModel = hiltViewModel(),
    onNavigateToExplore: (dateInMills: Long, mealPosition: Int) -> Unit,
    onNavigateToRecipeDetail: (dateInMillis: Long, mealPosition: Int, recipeId: Int) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var isAddMealPlanOptionsExpanded by remember { mutableStateOf(false) }

    val weekDays = getCurrentWeekDays()
    var selectedTab by remember {
        mutableIntStateOf(weekDays.indexOf(getCurrentDate()))
    }

    val targetNutrition by viewModel.targetNutrition.collectAsStateWithLifecycle()
    val mealPlan by viewModel.mealPlan.collectAsStateWithLifecycle()
    val shouldShowLoading = viewModel.shouldShowLoading
    val shouldShowError = viewModel.shouldShowError
    val onProcessItem = viewModel.onProcessItem
    val deleteRecipeState = viewModel.deleteRecipeState

    val successMessage =
        stringResource(R.string.meal_planner_meal_plan_snackbar_delete_success_message)
    val failMessage = stringResource(R.string.meal_planner_meal_plan_snackbar_delete_fail_message)

    LaunchedEffect(deleteRecipeState) {
        when (deleteRecipeState) {
            ResultState.SUCCESS -> {
                snackbarHostState.showSnackbar(
                    message = successMessage,
                    duration = SnackbarDuration.Short
                )
            }

            ResultState.FAILURE -> {
                snackbarHostState.showSnackbar(
                    message = failMessage,
                    duration = SnackbarDuration.Short
                )
            }

            ResultState.NONE -> { /* Do nothing */
            }
        }
    }

    Scaffold(
        topBar = {
            Column {
                MealPlannerTopBar(
                    modifier = Modifier.clickable( // Makes the scrim clickable
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null, // No visual feedback for scrim click
                        onClick = {
                            // This is where the "click anywhere on screen" action happens
                            isAddMealPlanOptionsExpanded = false
                        }
                    ),
                    selectedTabIndex = selectedTab,
                    weekDays = weekDays,
                    onDateSelected = { selectedTab = it },
                    onCreateNewMealPlan = { viewModel.createMealPlan() },
                    onClearMealPlan = { viewModel.clearMealPlan() }
                )

                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 2.dp,
                    color = GreyEBEBEB
                )
            }
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        AppSnackBar(
                            modifier = Modifier.padding(top = 36.dp),
                            message = it.visuals.message,
                            state = when (deleteRecipeState) {
                                ResultState.SUCCESS -> SnackBarState.SUCCESS
                                else -> SnackBarState.FAILURE
                            }
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            if (!shouldShowLoading && !shouldShowError) {
                AppFloatingButton(
                    isExpanded = isAddMealPlanOptionsExpanded,
                    actions = listOf(
                        FloatingAction(
                            label = stringResource(R.string.meal_planner_meal_plan_breakfast_label),
                            icon = drawable.img_breakfast,
                            action = {
                                onNavigateToExplore(
                                    weekDays[selectedTab].toEpochSeconds(),
                                    1
                                )
                            }
                        ),
                        FloatingAction(
                            label = stringResource(R.string.meal_planner_meal_plan_lunch_label),
                            icon = drawable.img_lunch,
                            action = {
                                onNavigateToExplore(
                                    weekDays[selectedTab].toEpochSeconds(),
                                    2
                                )
                            }
                        ),
                        FloatingAction(
                            label = stringResource(R.string.meal_planner_meal_plan_dinner_label),
                            icon = drawable.img_dinner,
                            action = {
                                onNavigateToExplore(
                                    weekDays[selectedTab].toEpochSeconds(),
                                    3
                                )
                            }
                        )
                    ),
                    onClick = { isAddMealPlanOptionsExpanded = !isAddMealPlanOptionsExpanded }
                )
            }
        }
    ) { innerPadding ->
        if (shouldShowLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LoadingIndicator(
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }

        if (shouldShowError) {
            Column {
                MealPlanInstruction(
                    isNetworkError = true,
                    instruction = stringResource(R.string.meal_planner_meal_plan_internet_error_text),
                    image = drawable.img_vector_internet_error,
                    onReload = { viewModel.getMealPlan() }
                )
            }
        }

        if (!shouldShowLoading && !shouldShowError) {
            val mealDay = mealPlan.firstOrNull { it.date == weekDays[selectedTab] }
            mealDay?.let { meal ->
                if (mealDay.breakfast.isNotEmpty() || mealDay.lunch.isNotEmpty() || mealDay.dinner.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(White)
                            .verticalScroll(rememberScrollState())
                            .padding(innerPadding),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        targetNutrition?.let { nutrition ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 24.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                meal.nutritionSummary.forEachIndexed { index, amount ->
                                    val color = when (index) {
                                        0 -> GreenA1CE50
                                        1 -> RedFF4950
                                        2 -> OrangeFB880C
                                        else -> Blue05A6F1
                                    }
                                    val nutrientName = stringResource(
                                        when (index) {
                                            0 -> R.string.meal_planner_nutrition_progress_nutrient_name_calories
                                            1 -> R.string.meal_planner_nutrition_progress_nutrient_name_carbs
                                            2 -> R.string.meal_planner_nutrition_progress_nutrient_name_protein
                                            else -> R.string.meal_planner_nutrition_progress_nutrient_name_fat
                                        }
                                    )
                                    val targetAmount = when (index) {
                                        0 -> nutrition.calories
                                        1 -> nutrition.carbs
                                        2 -> nutrition.protein
                                        else -> nutrition.fat
                                    }
                                    MealNutrientProgress(
                                        progressIndicatorColor = color,
                                        nutrientName = nutrientName,
                                        currentAmount = amount,
                                        targetAmount = targetAmount
                                    )
                                }
                            }
                        }

                        if (meal.breakfast.isNotEmpty()) {
                            MealSection(
                                meal = stringResource(R.string.meal_planner_meal_plan_breakfast_label),
                                calories = meal.breakfastNutrition[NUTRIENTS_CALORIES_INDEX],
                                recipes = mealDay.breakfast,
                                onProcessItem = onProcessItem,
                                onNavigateToExplore = {
                                    onNavigateToExplore(
                                        meal.date.toEpochSeconds(),
                                        1
                                    )
                                },
                                onNavigateToRecipeDetail = { recipeId ->
                                    onNavigateToRecipeDetail(
                                        meal.date.toEpochMillis(),
                                        1,
                                        recipeId
                                    )
                                },
                                onDeleteRecipe = {
                                    viewModel.deleteRecipeFromMealPlan(
                                        meal.date,
                                        1,
                                        it
                                    )
                                }
                            )
                        }
                        if (mealDay.lunch.isNotEmpty()) {
                            MealSection(
                                meal = stringResource(R.string.meal_planner_meal_plan_lunch_label),
                                calories = meal.lunchNutrition[NUTRIENTS_CALORIES_INDEX],
                                recipes = mealDay.lunch,
                                onProcessItem = onProcessItem,
                                onNavigateToExplore = {
                                    onNavigateToExplore(
                                        meal.date.toEpochSeconds(),
                                        2
                                    )
                                },
                                onNavigateToRecipeDetail = { recipeId ->
                                    onNavigateToRecipeDetail(
                                        meal.date.toEpochMillis(),
                                        2,
                                        recipeId
                                    )
                                },
                                onDeleteRecipe = {
                                    viewModel.deleteRecipeFromMealPlan(
                                        meal.date,
                                        2,
                                        it
                                    )
                                }
                            )
                        }
                        if (mealDay.dinner.isNotEmpty()) {
                            MealSection(
                                meal = stringResource(R.string.meal_planner_meal_plan_dinner_label),
                                calories = meal.dinnerNutrition[NUTRIENTS_CALORIES_INDEX],
                                recipes = mealDay.dinner,
                                onProcessItem = onProcessItem,
                                onNavigateToExplore = {
                                    onNavigateToExplore(
                                        meal.date.toEpochSeconds(),
                                        3
                                    )
                                },
                                onNavigateToRecipeDetail = { recipeId ->
                                    onNavigateToRecipeDetail(
                                        meal.date.toEpochMillis(),
                                        3,
                                        recipeId
                                    )
                                },
                                onDeleteRecipe = {
                                    viewModel.deleteRecipeFromMealPlan(
                                        meal.date,
                                        3,
                                        it
                                    )
                                }
                            )
                        }
                    }
                } else {
                    MealPlanInstruction(
                        modifier = Modifier.clickable( // Makes the scrim clickable
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null, // No visual feedback for scrim click
                            onClick = {
                                // This is where the "click anywhere on screen" action happens
                                isAddMealPlanOptionsExpanded = false
                            }
                        ),
                        instruction = stringResource(R.string.meal_planner_meal_plan_no_plan_today_text),
                        image = R.drawable.img_vector_no_meal_plan
                    )
                }
            } ?: run {
                MealPlanInstruction(
                    modifier = Modifier.clickable( // Makes the scrim clickable
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null, // No visual feedback for scrim click
                        onClick = {
                            // This is where the "click anywhere on screen" action happens
                            isAddMealPlanOptionsExpanded = false
                        }
                    ),
                    instruction = stringResource(R.string.meal_planner_meal_plan_no_plan_today_text),
                    image = R.drawable.img_vector_no_meal_plan
                )
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealPlannerTopBar(
    modifier: Modifier = Modifier,
    selectedTabIndex: Int,
    weekDays: List<LocalDate>,
    onDateSelected: (Int) -> Unit,
    onCreateNewMealPlan: () -> Unit,
    onClearMealPlan: () -> Unit
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    if (showBottomSheet) {
        ModalBottomSheet(
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.background,
            onDismissRequest = { showBottomSheet = false }
        ) {
            ActionBottomSheet(
                actions = listOf(
                    SheetAction(
                        label = stringResource(R.string.meal_planner_bottom_sheet_create_new_meal_plan),
                        icon = painterResource(R.drawable.ic_fork_spoon),
                        action = {
                            showBottomSheet = false
                            onCreateNewMealPlan()
                        }
                    ),
                    SheetAction(
                        label = stringResource(R.string.meal_planner_bottom_sheet_clear_meal_plan),
                        icon = painterResource(R.drawable.ic_no_meal_plan),
                        action = {
                            showBottomSheet = false
                            onClearMealPlan()
                        }
                    )
                )
            )
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(top = 24.dp),
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AppText(
                text = getGreetingText(),
                textStyle = Typography.labelLarge
            )

            IconButton(
                onClick = { showBottomSheet = true }
            ) {
                Icon(
                    painter = painterResource(drawable.ic_settings),
                    tint = Black374957,
                    contentDescription = null
                )
            }
        }

        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
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
}

@Composable
fun MealNutrientProgress(
    progressIndicatorColor: Color,
    nutrientName: String,
    currentAmount: Int,
    targetAmount: Int,
) {
    var targetSweepAngle by remember { mutableFloatStateOf(0F) }
    val animatedSweepAngle by animateFloatAsState(
        targetValue = targetSweepAngle,
        animationSpec = tween(durationMillis = 800)
    )

    LaunchedEffect(currentAmount) {
        targetSweepAngle = currentAmount * 360F / targetAmount
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {
            AppText(
                modifier = Modifier.align(Alignment.Center),
                text = currentAmount.toString(),
                textStyle = Typography.bodyMedium
            )

            val stroke = with(LocalDensity.current) {
                Stroke(
                    width = 3.dp.toPx(),
                    cap = StrokeCap.Round,
                )
            }

            // Under circular progress
            Canvas(
                modifier = Modifier
                    .size(50.dp)
                    .align(Alignment.Center)
            ) {
                val innerRadius = (size.minDimension - stroke.width) / 2
                val halfSize = size / 2.0f
                val topLeft = Offset(
                    halfSize.width - innerRadius,
                    halfSize.height - innerRadius
                )
                val size = Size(innerRadius * 2, innerRadius * 2)

                drawArc(
                    color = GreyDADADA,
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
                modifier = Modifier
                    .size(50.dp)
                    .align(Alignment.Center)
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

        AppText(
            modifier = Modifier.padding(top = 4.dp),
            text = nutrientName,
            textStyle = Typography.bodyMedium,
            color = Grey808993
        )
    }
}

@Composable
fun MealSection(
    meal: String,
    calories: Int,
    recipes: List<MealRecipe>,
    onProcessItem: Int,
    onNavigateToExplore: () -> Unit,
    onNavigateToRecipeDetail: (Int) -> Unit,
    onDeleteRecipe: (Int) -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = 24.dp,
                horizontal = 16.dp
            )
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
                top.linkTo(textMeal.bottom, margin = 8.dp)
                start.linkTo(parent.start)
            },
            text = stringResource(R.string.meal_planner_meal_plan_calories_text, calories),
            textStyle = Typography.bodyLarge,
            color = Green86BF3E
        )

        Icon(
            modifier = Modifier
                .size(28.dp)
                .clickable { onNavigateToExplore() }
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
            recipes.forEach { recipe ->
                MealItem(
                    recipe = recipe,
                    showLoading = recipe.mealId == onProcessItem,
                    onRecipeClick = { onNavigateToRecipeDetail(it) },
                    onDeleteRecipe = { onDeleteRecipe(recipe.mealId) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionBottomSheet(
    actions: List<SheetAction>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp)
    ) {
        actions.forEach { action ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .clickable { action.action() }
                    .padding(
                        horizontal = 32.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(28.dp),
                    painter = action.icon,
                    tint = Black374957,
                    contentDescription = null
                )

                AppText(
                    modifier = Modifier.padding(start = 32.dp),
                    text = action.label,
                    textStyle = Typography.titleSmall
                )
            }
        }
    }
}

@Composable
fun MealPlanInstruction(
    modifier: Modifier = Modifier,
    isNetworkError: Boolean = false,
    instruction: String,
    image: Int,
    onReload: () -> Unit = {}
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.size(250.dp),
                painter = painterResource(image),
                contentDescription = null
            )

            AppText(
                modifier = Modifier.padding(top = 20.dp),
                text = instruction,
                textStyle = Typography.bodyLarge,
                color = Grey7F000000
            )

            if (isNetworkError) {
                Button(
                    modifier = Modifier
                        .height(80.dp)
                        .padding(top = 36.dp)
                        .align(Alignment.CenterHorizontally),
                    onClick = { onReload() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Green91C747
                    )
                ) {
                    AppText(
                        text = stringResource(R.string.meal_planner_meal_plan_button_text_reload),
                        color = White,
                        textStyle = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }
        }
    }
}

@Preview()
@Composable
fun MealPlannerTopBarPreview() {
    MealPlannerTopBar(
        selectedTabIndex = 0,
        weekDays = getCurrentWeekDays(),
        onDateSelected = {},
        onCreateNewMealPlan = {},
        onClearMealPlan = {}
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun MealNutrientProgressPreview() {
    MealNutrientProgress(
        progressIndicatorColor = GreenA1CE50,
        nutrientName = "Cal",
        currentAmount = 158,
        targetAmount = 224
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun MealSectionPreview() {
    MealSection(
        meal = "Breakfast",
        calories = 500,
        onProcessItem = 0,
        recipes = listOf(
            MealRecipe(
                mealId = 1,
                recipeId = 1,
                imageUrl = "",
                name = "Spaghetti Bolognese ".repeat(3),
                cookTime = 30,
                servings = 4,
                calories = 1000,
                carbs = 100,
                protein = 100,
                fat = 100
            )
        ),
        onNavigateToExplore = {},
        onNavigateToRecipeDetail = {},
        onDeleteRecipe = {}
    )
}

@Preview(showBackground = true)
@Composable
fun ActionBottomSheetPreview() {
    ActionBottomSheet(
        actions = listOf(
            SheetAction(
                label = "Create new plan",
                icon = painterResource(R.drawable.ic_fork_spoon),
                action = {}
            )
        )
    )
}

@Preview(showBackground = true)
@Composable
fun NoMealPlanInstructionPreview() {
    MealPlanInstruction(
        instruction = stringResource(R.string.meal_planner_meal_plan_no_plan_today_text),
        image = R.drawable.img_vector_no_meal_plan
    )
}
