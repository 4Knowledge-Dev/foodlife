package com.forknowledge.feature.onboarding

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.forknowledge.feature.onboarding.ui.SurveyScreen
import com.forknowledge.feature.onboarding.ui.WelcomeScreen
import kotlinx.serialization.Serializable

@Serializable
data object OnboardingRoute

@Serializable
data object WelcomeRoute

@Serializable
data object SurveyRoute

fun NavGraphBuilder.onboardingNavGraph(navController: NavController) {
    navigation<OnboardingRoute>(startDestination = WelcomeRoute) {

        welcomeScreen(navController)
        surveyScreen(navController)
    }
}

fun NavGraphBuilder.welcomeScreen(navController: NavController) {
    composable<WelcomeRoute> {
        WelcomeScreen { navController.navigate(SurveyRoute) }
    }
}

fun NavGraphBuilder.surveyScreen(navController: NavController) {
    composable<SurveyRoute> { SurveyScreen() }
}
