package com.forknowledge.feature.authentication

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import androidx.navigation.navigation
import com.forknowledge.feature.authentication.ui.AuthenticationViewModel
import com.forknowledge.feature.authentication.ui.screen.SignInOptionScreen
import com.forknowledge.feature.authentication.ui.screen.SignInWithEmailScreen
import com.forknowledge.feature.authentication.ui.screen.SignUpWithEmailScreen
import com.forknowledge.feature.authentication.ui.screen.navigateToSignInWithEmail
import com.forknowledge.feature.authentication.ui.screen.navigateToSignUpWithEmail
import com.forknowledge.feature.onboarding.navigateToOnboarding
import com.forknowledge.feature.planner.navigateToPlannerRoute
import kotlinx.serialization.Serializable

@Serializable
data object AuthenticationRoute

@Serializable
data object SignInOptionsRoute

@Serializable
data object SignInWithEmailRoute

@Serializable
data object SignUpWithEmailRoute

fun NavController.navigateToAuthentication() = navigate(AuthenticationRoute) {
    popUpTo(graph.findStartDestination().id) {
        inclusive = true
    }
}

fun NavGraphBuilder.authenticationNavGraph(
    navController: NavController
) {
    navigation<AuthenticationRoute>(startDestination = SignInOptionsRoute) {

        signInOptionsScreen(navController)

        signInWithEmailScreen(navController)

        signUpWithEmailScreen(navController)
    }
}

fun NavGraphBuilder.signInOptionsScreen(navController: NavController) {
    composable<SignInOptionsRoute> { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(AuthenticationRoute)
        }
        SignInOptionScreen(
            viewModel = hiltViewModel<AuthenticationViewModel>(parentEntry),
            signInWithEmailClicked = navController::navigateToSignInWithEmail,
            onNavigateToOnboarding = { navController.navigateToOnboarding() },
            onNavigateToPlanner = { navController.navigateToPlannerRoute() }
        )
    }
}

fun NavGraphBuilder.signInWithEmailScreen(navController: NavController) {
    composable<SignInWithEmailRoute> { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(AuthenticationRoute)
        }
        SignInWithEmailScreen(
            viewModel = hiltViewModel<AuthenticationViewModel>(parentEntry),
            onBackClicked = navController::popBackStack,
            onNavigateToRegisterClicked = navController::navigateToSignUpWithEmail,
        )
    }
}

fun NavGraphBuilder.signUpWithEmailScreen(navController: NavController) {
    composable<SignUpWithEmailRoute> { backStackEntry ->
        val navOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id)
        }
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(AuthenticationRoute)
        }
        SignUpWithEmailScreen(
            viewModel = hiltViewModel<AuthenticationViewModel>(parentEntry),
            onBackClicked = navController::popBackStack,
            onNavigateToLoginClicked = { navController.navigateToSignInWithEmail(navOptions) },
            onNavigateToOnboarding = { navController.navigateToOnboarding() }
        )
    }
}
