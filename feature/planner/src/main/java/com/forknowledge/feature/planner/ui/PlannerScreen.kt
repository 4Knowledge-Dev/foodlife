package com.forknowledge.feature.planner.ui

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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.forknowledge.core.common.Result
import com.forknowledge.core.common.extension.toDayAndDateString
import com.forknowledge.core.common.extension.toEpochSeconds
import com.forknowledge.core.ui.R.drawable
import com.forknowledge.core.ui.theme.Black063336
import com.forknowledge.core.ui.theme.Black374957
import com.forknowledge.core.ui.theme.Green91C747
import com.forknowledge.core.ui.theme.GreenA1CE50
import com.forknowledge.core.ui.theme.Grey7F000000
import com.forknowledge.core.ui.theme.Grey808993
import com.forknowledge.core.ui.theme.GreyEBEBEB
import com.forknowledge.core.ui.theme.Typography
import com.forknowledge.core.ui.theme.component.AppFloatingButton
import com.forknowledge.core.ui.theme.component.AppSnackBar
import com.forknowledge.core.ui.theme.component.AppText
import com.forknowledge.core.ui.theme.component.LoadingIndicator
import com.forknowledge.core.ui.theme.state.FloatingAction
import com.forknowledge.core.ui.theme.state.SnackBarState
import com.forknowledge.feature.model.MealRecipe
import com.forknowledge.feature.planner.MealAction
import com.forknowledge.feature.planner.R
import com.forknowledge.feature.planner.getCurrentDate
import com.forknowledge.feature.planner.getCurrentWeekDays
import java.time.LocalDate

@Composable
fun PlannerScreen(
    viewModel: MealPlannerViewModel = hiltViewModel(),
    onNavigateToExplore: (Long, Int) -> Unit,
    onNavigateToRecipeDetail: (Int) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var isAddMealPlanOptionsExpanded by remember { mutableStateOf(false) }

    val weekDays = getCurrentWeekDays()
    var selectedTab by remember {
        mutableIntStateOf(weekDays.indexOf(getCurrentDate()))
    }
    val mealPlan by viewModel.mealPlan.collectAsStateWithLifecycle()
    val shouldShowLoading = viewModel.shouldShowLoading
    val shouldShowError = viewModel.shouldShowError
    val onProcessItem = viewModel.onProcessItem
    val deleteRecipeState = viewModel.deleteRecipeState

    val successMessage =
        stringResource(R.string.meal_planner_meal_plan_snackbar_delete_success_message)
    val failMessage = stringResource(R.string.meal_planner_meal_plan_snackbar_delete_fail_message)

    LaunchedEffect(deleteRecipeState) {
        snapshotFlow { deleteRecipeState }
            .collect { state ->
                when (state) {
                    is Result.Success -> {
                        snackbarHostState.showSnackbar(
                            message = successMessage,
                            duration = SnackbarDuration.Short
                        )
                    }

                    is Result.Error -> {
                        snackbarHostState.showSnackbar(
                            message = failMessage,
                            duration = SnackbarDuration.Short
                        )
                    }

                    is Result.Loading -> { /* Do nothing */
                    }
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
                    onDateSelected = { selectedTab = it }
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
                                is Result.Success -> SnackBarState.SUCCESS
                                is Result.Error -> SnackBarState.FAILURE
                                is Result.Loading -> SnackBarState.NONE
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
                        if (meal.breakfast.isNotEmpty()) {
                            MealSection(
                                meal = stringResource(R.string.meal_planner_meal_plan_breakfast_label),
                                recipes = mealDay.breakfast,
                                onProcessItem = onProcessItem,
                                onNavigateToExplore = {
                                    onNavigateToExplore(
                                        meal.date.toEpochSeconds(),
                                        1
                                    )
                                },
                                onNavigateToRecipeDetail = { onNavigateToRecipeDetail(it) },
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
                                recipes = mealDay.lunch,
                                onProcessItem = onProcessItem,
                                onNavigateToExplore = {
                                    onNavigateToExplore(
                                        meal.date.toEpochSeconds(),
                                        2
                                    )
                                },
                                onNavigateToRecipeDetail = { onNavigateToRecipeDetail(it) },
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
                                recipes = mealDay.dinner,
                                onProcessItem = onProcessItem,
                                onNavigateToExplore = {
                                    onNavigateToExplore(
                                        meal.date.toEpochSeconds(),
                                        3
                                    )
                                },
                                onNavigateToRecipeDetail = { onNavigateToRecipeDetail(it) },
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
    onDateSelected: (Int) -> Unit
) {
    Row(
        modifier = modifier
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
        val (textMeal, actionIcon, mealList) = createRefs()

        AppText(
            modifier = Modifier.constrainAs(textMeal) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
            },
            text = meal,
            textStyle = Typography.titleMedium
        )

        Icon(
            modifier = Modifier
                .size(28.dp)
                .clickable { onNavigateToExplore() }
                .constrainAs(actionIcon) {
                    top.linkTo(textMeal.top)
                    bottom.linkTo(textMeal.bottom)
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
                    top.linkTo(textMeal.bottom, margin = 12.dp)
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
fun MealItem(
    recipe: MealRecipe,
    showLoading: Boolean,
    onRecipeClick: (Int) -> Unit,
    onDeleteRecipe: () -> Unit
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
                onDeleteRecipe = {
                    showBottomSheet = false
                    onDeleteRecipe()
                }
            )
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
            .clickable { onRecipeClick(recipe.recipeId) }

    ) {
        val (recipeImage, recipeName, cookTime, servings, actionIcon) = createRefs()

        AsyncImage(
            modifier = Modifier
                .size(
                    width = 130.dp,
                    height = 110.dp
                )
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
            model = recipe.imageUrl,
            error = painterResource(drawable.img_sample),
            contentScale = ContentScale.Crop,
            contentDescription = null
        )

        AppText(
            modifier = Modifier
                .padding(top = 4.dp)
                .widthIn(max = 190.dp)
                .constrainAs(recipeName) {
                    top.linkTo(recipeImage.top, margin = 4.dp)
                    start.linkTo(recipeImage.end, margin = 16.dp)
                    end.linkTo(parent.end)
                    horizontalBias = 0F
                },
            text = recipe.name,
            textStyle = Typography.labelMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        AppText(
            modifier = Modifier
                .constrainAs(cookTime) {
                    top.linkTo(recipeName.bottom)
                    bottom.linkTo(recipeImage.bottom, margin = 4.dp)
                    start.linkTo(recipeImage.end, margin = 16.dp)
                    end.linkTo(actionIcon.start)
                    horizontalBias = 0F
                },
            text = pluralStringResource(
                R.plurals.meal_planner_recipe_cook_time_text,
                recipe.cookTime,
                recipe.cookTime
            ),
            textStyle = Typography.bodySmall,
            color = Grey808993
        )

        AppText(
            modifier = Modifier
                .widthIn(max = 230.dp)
                .constrainAs(servings) {
                    top.linkTo(recipeName.bottom)
                    bottom.linkTo(recipeImage.bottom, margin = 4.dp)
                    start.linkTo(cookTime.end, margin = 24.dp)
                    end.linkTo(actionIcon.start)
                    horizontalBias = 0F
                },
            text = pluralStringResource(
                R.plurals.meal_planner_recipe_serving_text,
                recipe.servings,
                recipe.servings
            ),
            textStyle = Typography.bodySmall,
            color = Grey808993
        )

        if (showLoading) {
            LoadingIndicator(
                modifier = Modifier
                    .size(28.dp)
                    .clickable { showBottomSheet = true }
                    .constrainAs(actionIcon) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end, margin = 12.dp)
                    },
                strokeWidth = 3.dp
            )
        } else {
            Icon(
                modifier = Modifier
                    .size(28.dp)
                    .clickable { showBottomSheet = true }
                    .constrainAs(actionIcon) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end, margin = 12.dp)
                    },
                painter = painterResource(drawable.ic_options),
                tint = Black374957,
                contentDescription = null
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionBottomSheet(
    onDeleteRecipe: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp)
    ) {
        MealAction.entries.forEach { action ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clickable {
                        when (action) {
                            MealAction.SWAP -> {}
                            MealAction.DELETE -> onDeleteRecipe()
                        }
                    }
                    .padding(
                        horizontal = 32.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(28.dp),
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
        onDateSelected = {}
    )
}

@Preview()
@Composable
fun MealItemPreview() {
    MealItem(
        recipe = MealRecipe(
            mealId = 1,
            recipeId = 1,
            imageUrl = "",
            name = "Spaghetti Bolognese ".repeat(3),
            cookTime = 30,
            servings = 4,
        ),
        showLoading = true,
        onRecipeClick = {},
        onDeleteRecipe = {}
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun MealSectionPreview() {
    MealSection(
        meal = "Breakfast",
        onProcessItem = 0,
        recipes = listOf(
            MealRecipe(
                mealId = 1,
                recipeId = 1,
                imageUrl = "",
                name = "Spaghetti Bolognese ".repeat(3),
                cookTime = 30,
                servings = 4,
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
        onDeleteRecipe = {}
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
