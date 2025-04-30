package com.forknowledge.foodlife.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.forknowledge.feature.authentication.authenticationNestedGraph
import com.forknowledge.feature.onboarding.ONBOARDING_ROUTE
import com.forknowledge.feature.onboarding.onboardingNavGraph
import com.forknowledge.foodlife.AppState
import com.forknowledge.foodlife.rememberAppState

@Composable
fun AppScreen(appState: AppState = rememberAppState()) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { },
        bottomBar = { },
        snackbarHost = { SnackbarHost(hostState = appState.snackbarHostState) }
    ) { padding ->

        NavHost(
            modifier = Modifier.padding(padding),
            navController = appState.navController,
            startDestination = ONBOARDING_ROUTE
        ) {

            authenticationNestedGraph(navController = appState.navController)
            onboardingNavGraph(navController = appState.navController)
        }
    }
}
