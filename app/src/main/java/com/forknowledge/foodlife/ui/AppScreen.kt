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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.forknowledge.core.ui.theme.Typography
import com.forknowledge.core.ui.theme.component.AppText
import com.forknowledge.feature.authentication.AuthenticationRoute
import com.forknowledge.feature.authentication.authenticationNavGraph
import com.forknowledge.feature.explore.ExploreRoute
import com.forknowledge.feature.explore.ExploreScreen
import com.forknowledge.feature.nutrient.NutrientRoute
import com.forknowledge.feature.nutrient.nutrient.NutrientScreen
import com.forknowledge.feature.nutrient.search.SearchRoute
import com.forknowledge.feature.nutrient.search.SearchScreen
import com.forknowledge.feature.nutrient.ui.LogFoodRoute
import com.forknowledge.feature.nutrient.ui.LogFoodScreen
import com.forknowledge.feature.onboarding.OnboardingRoute
import com.forknowledge.feature.onboarding.onboardingNavGraph
import com.forknowledge.feature.planner.PlannerRoute
import com.forknowledge.feature.planner.PlannerScreen
import com.forknowledge.foodlife.R
import com.forknowledge.foodlife.MainViewModel
import com.forknowledge.core.data.model.UserAuthState

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
                    onNavigateToLogFood = { meal, hasLoggedFood, date ->
                        appState.navController.navigate(
                            LogFoodRoute(meal, hasLoggedFood, date)
                        )
                    }
                )
            }
            composable<PlannerRoute> { PlannerScreen() }
            composable<ExploreRoute> { ExploreScreen() }
            composable<LogFoodRoute> { backStackEntry ->
                val meal = backStackEntry.toRoute<LogFoodRoute>()
                LogFoodScreen(
                    meal = meal.meal,
                    hasLoggedFood = meal.hasLoggedRecipe,
                    dateInMillis = meal.dateInMillis,
                    onNavigateToSearch = { meal, hasLoggedFood, date ->
                        appState.navController.navigate(
                            SearchRoute(meal, hasLoggedFood, date)
                        )
                    },
                    onNavigateBack = { appState.navController.popBackStack() }
                )
            }
            composable<SearchRoute> { backStackEntry ->
                val data = backStackEntry.toRoute<SearchRoute>()
                SearchScreen(
                    meal = data.meal,
                    hasLoggedFood = data.hasLoggedFood,
                    dateInMillis = data.dateInMillis,
                    onNavigateBack = { appState.navController.popBackStack() }
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
