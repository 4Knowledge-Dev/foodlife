package com.forknowledge.feature.authentication

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.forknowledge.feature.authentication.ui.screen.SIGN_IN_OPTIONS_ROUTE
import com.forknowledge.feature.authentication.ui.screen.signInOptionsScreen
import com.forknowledge.feature.authentication.ui.screen.signInWithEmailScreen
import com.forknowledge.feature.authentication.ui.screen.signUpWithEmailScreen

const val AUTHENTICATION_ROUTE = "authentication"

fun NavGraphBuilder.authenticationNestedGraph(
    navController: NavController
) {
    navigation(
        startDestination = SIGN_IN_OPTIONS_ROUTE,
        route = AUTHENTICATION_ROUTE
    ) {

        signInOptionsScreen(navController)

        signInWithEmailScreen(navController)

        signUpWithEmailScreen(navController)
    }
}
