package com.forknowledge.feature.onboarding

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.forknowledge.feature.onboarding.ui.WELCOME_ROUTE
import com.forknowledge.feature.onboarding.ui.surveyScreen
import com.forknowledge.feature.onboarding.ui.welcomeScreen

const val ONBOARDING_ROUTE = "onboarding"

fun NavGraphBuilder.onboardingNavGraph(navController: NavController) {
    navigation(
        startDestination = WELCOME_ROUTE,
        route = ONBOARDING_ROUTE
    ) {

        welcomeScreen(navController)
        surveyScreen(navController)
    }
}
