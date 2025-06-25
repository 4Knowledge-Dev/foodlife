package com.forknowledge.foodlife.ui

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.forknowledge.core.ui.theme.Typography
import com.forknowledge.core.ui.theme.component.AppText
import com.forknowledge.feature.authentication.authenticationNavGraph
import com.forknowledge.feature.explore.ExploreRoute
import com.forknowledge.feature.explore.ExploreSearchRoute
import com.forknowledge.feature.explore.ui.ExploreScreen
import com.forknowledge.feature.explore.ui.ExploreSearchScreen
import com.forknowledge.feature.nutrient.AdditionalNutritionRoute
import com.forknowledge.feature.nutrient.InsightsRoute
import com.forknowledge.feature.nutrient.NutrientGroupRoute
import com.forknowledge.feature.nutrient.NutrientRoute
import com.forknowledge.feature.nutrient.SearchRoute
import com.forknowledge.feature.nutrient.StatisticsRoute
import com.forknowledge.feature.nutrient.dailyinsights.DailyInsightsScreen
import com.forknowledge.feature.nutrient.nutrient.NutrientScreen
import com.forknowledge.feature.nutrient.search.SearchScreen
import com.forknowledge.feature.nutrient.statistics.AdditionalNutritionScreen
import com.forknowledge.feature.nutrient.statistics.NutrientGroupScreen
import com.forknowledge.feature.nutrient.statistics.StatisticsScreen
import com.forknowledge.feature.nutrient.tracking.LogFoodRoute
import com.forknowledge.feature.nutrient.tracking.LogFoodScreen
import com.forknowledge.feature.onboarding.onboardingNavGraph
import com.forknowledge.feature.planner.PlannerRoute
import com.forknowledge.feature.planner.navigateToPlannerRoute
import com.forknowledge.feature.planner.ui.PlannerScreen
import com.forknowledge.feature.recipe.RecipeRoute
import com.forknowledge.feature.recipe.RecipeScreen
import com.forknowledge.foodlife.R

@Composable
fun AppScreen(
    appState: AppState,
    startDestinationRoute: Any,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (appState.currentTopLevelDestination != null) {
                AppBottomBar(
                    topLevelDestinations = appState.topLevelDestinations,
                    onTabChanged = appState::navigateToTopLevelDestination
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->

        val isOffline by appState.isOffline.collectAsStateWithLifecycle()
        val internetErrorMessage = stringResource(id = R.string.internet_error_not_connected)

        LaunchedEffect(isOffline) {
            if (isOffline) {
                snackbarHostState.showSnackbar(
                    message = internetErrorMessage,
                    duration = SnackbarDuration.Indefinite
                )
            }
        }

        NavHost(
            modifier = Modifier
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding),
            navController = appState.navController,
            startDestination = startDestinationRoute,
        ) {

            authenticationNavGraph(navController = appState.navController)
            onboardingNavGraph(navController = appState.navController)
            composable<NutrientRoute> {
                NutrientScreen(
                    onNavigateToLogFood = { mealPosition, date ->
                        appState.navController.navigate(
                            LogFoodRoute(mealPosition, date)
                        )
                    },
                    onNavigateToDailyInsights = { dateInMillis ->
                        appState.navController.navigate(
                            InsightsRoute(dateInMillis)
                        )
                    },
                    onNavigateToNutrientGroup = {
                        appState.navController.navigate(NutrientGroupRoute)
                    }
                )
            }
            composable<PlannerRoute> {
                PlannerScreen(
                    onNavigateToExplore = { date, mealPosition ->
                        appState.navController.navigate(
                            ExploreRoute(
                                isAddMealPlanProcess = true,
                                mealPosition = mealPosition,
                                dateInMillis = date
                            )
                        )
                    },
                    onNavigateToRecipeDetail = { recipeId ->
                        appState.navController.navigate(RecipeRoute(recipeId))
                    }
                )
            }
            composable<ExploreRoute> { backStackEntry ->
                val data = backStackEntry.toRoute<ExploreRoute>()
                ExploreScreen(
                    isAddMealPlanProcess = data.isAddMealPlanProcess,
                    onNavigateToSearch = {
                        appState.navController.navigate(
                            ExploreSearchRoute(
                                isAddMealPlanProcess = data.isAddMealPlanProcess,
                                mealPosition = data.mealPosition,
                                dateInMillis = data.dateInMillis
                            )
                        )
                    },
                    onNavigateBack = appState.navController::popBackStack
                )
            }
            composable<LogFoodRoute> { backStackEntry ->
                val meal = backStackEntry.toRoute<LogFoodRoute>()
                LogFoodScreen(
                    mealPosition = meal.mealPosition,
                    dateInMillis = meal.dateInMillis,
                    onNavigateToSearch = { mealPosition, date ->
                        appState.navController.navigate(
                            SearchRoute(mealPosition, date)
                        )
                    },
                    onNavigateBack = { appState.navController.popBackStack() }
                )
            }
            composable<SearchRoute> { backStackEntry ->
                val data = backStackEntry.toRoute<SearchRoute>()
                SearchScreen(
                    mealPosition = data.mealPosition,
                    dateInMillis = data.dateInMillis,
                    onNavigateBack = { appState.navController.popBackStack() },
                    onNavigateToNutrient = {
                        appState.navigateToTopLevelDestination(
                            TopLevelDestination.NUTRIENT
                        )
                    }
                )
            }
            composable<ExploreSearchRoute> { backStackEntry ->
                val data = backStackEntry.toRoute<ExploreSearchRoute>()
                ExploreSearchScreen(
                    isAddMealPlanProcess = data.isAddMealPlanProcess,
                    mealPosition = data.mealPosition,
                    dateInMillis = data.dateInMillis,
                    onNavigateToMealPlan = {
                        appState.navController.navigateToPlannerRoute()
                    },
                    onNavigateBack = { appState.navController.popBackStack() }
                )
            }
            composable<InsightsRoute> { backStackEntry ->
                val data = backStackEntry.toRoute<InsightsRoute>()
                DailyInsightsScreen(
                    dateInMillis = data.dateInMillis,
                    onNavigateBack = appState.navController::popBackStack
                )
            }
            composable<NutrientGroupRoute> {
                NutrientGroupScreen(
                    onNavigateToAdditionalNutrition = { name, type ->
                        appState.navController.navigate(
                            AdditionalNutritionRoute(name, type)
                        )
                    },
                    onNavigateToStatistics = { name, type ->
                        appState.navController.navigate(StatisticsRoute(name, type))
                    },
                    onNavigateBack = appState.navController::popBackStack
                )
            }
            composable<AdditionalNutritionRoute> { backStackEntry ->
                val data = backStackEntry.toRoute<AdditionalNutritionRoute>()
                AdditionalNutritionScreen(
                    groupName = data.groupName,
                    nutritionType = data.nutritionType,
                    onNavigateToStatistics = { nutritionName, nutritionType ->
                        appState.navController.navigate(
                            StatisticsRoute(nutritionName, nutritionType)
                        )
                    },
                    onNavigateBack = appState.navController::popBackStack
                )
            }
            composable<StatisticsRoute> { backStackEntry ->
                val data = backStackEntry.toRoute<StatisticsRoute>()
                StatisticsScreen(
                    nutritionName = data.nutrientName,
                    nutritionType = data.type,
                    onNavigateBack = appState.navController::popBackStack
                )
            }
            composable<RecipeRoute> { backStackEntry ->
                val recipeId = backStackEntry.toRoute<RecipeRoute>().recipeId
                RecipeScreen(
                    recipeId = recipeId,
                    onNavigateBack = appState.navController::popBackStack
                )
            }
        }
    }
}

@Composable
fun AppBottomBar(
    topLevelDestinations: List<TopLevelDestination>,
    onTabChanged: (TopLevelDestination) -> Unit
) {

    var selectedItem by remember {
        mutableIntStateOf(0)
    }

    NavigationBar(
        containerColor = White,
        modifier = Modifier.drawBehind {
            val shadowHeight = 2.dp.toPx()
            val shadowColor = Color.Black.copy(alpha = 0.2f)

            drawLine(
                color = shadowColor,
                start = Offset(0f, 0f),
                end = Offset(size.width, 0f),
                strokeWidth = shadowHeight
            )
        }
    ) {
        topLevelDestinations.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    onTabChanged(item)
                },
                icon = {
                    Icon(
                        painter = painterResource(
                            id = if (selectedItem == index) item.selectedIcon else item.unselectedIcon
                        ),
                        contentDescription = null
                    )
                },
                label = {
                    AppText(
                        text = stringResource(item.titleText),
                        textStyle = Typography.bodySmall
                    )
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppBottomBarPreview() {
    AppBottomBar(
        topLevelDestinations = TopLevelDestination.entries,
        onTabChanged = {}
    )
}
