package com.forknowledge.foodlife

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun rememberAppState(
    navController: NavHostController = rememberNavController(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
): AppState {
    return remember {
        AppState(
            navController = navController,
            snackbarHostState = snackbarHostState,
            coroutineScope = coroutineScope
        )
    }
}

class AppState(
    val navController: NavHostController,
    val snackbarHostState: SnackbarHostState,
    val coroutineScope: CoroutineScope,
) {

    //val topLevelDestinationEntries = TopLevelDestination.entries

    val currentDestination: NavDestination?
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination

    /*val topLevelDestination: TopLevelDestination?
        @Composable get() = when (currentDestination?.route) {
            else -> null
    }*/

    /**
     * UI logic for navigating to a top level destination in the app. Top level destinations have
     * only one copy of the destination of the back stack, and save and restore state whenever you
     * navigate to and from it.
     *
     * @param topLevelDestination: The destination the app needs to navigate to.
     */
    /*fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        val navOptions = navOptions {
            popUpTo(navController.graph.startDestinationId) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }


    }*/

    /**
     * Show an app snackbar
     * @param [message] - text to be shown in the Snackbar
     * @param [actionLabel] - optional action label to show as button in the Snackbar
     * @param [dismissAction] - a boolean to show a dismiss action in the Snackbar.
     * This is recommended to be set to true for better accessibility when a Snackbar is set with
     * a SnackbarDuration.Indefinite
     * @param [duration] - duration to control how long snackbar will be shown in SnackbarHost,
     * either SnackbarDuration.Short, SnackbarDuration.Long or SnackbarDuration.Indefinite.
     */
    fun showSnackBar(
        message: String,
        actionLabel: String? = null,
        dismissAction: Boolean = false,
        duration: SnackbarDuration = SnackbarDuration.Short
    ) = coroutineScope.launch {
        snackbarHostState.showSnackbar(
            message = message,
            actionLabel = actionLabel,
            withDismissAction = dismissAction,
            duration = duration
        )
    }
}
