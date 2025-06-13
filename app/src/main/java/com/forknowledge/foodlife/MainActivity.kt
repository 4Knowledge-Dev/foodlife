package com.forknowledge.foodlife

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.forknowledge.core.data.NetworkManager
import com.forknowledge.core.ui.theme.FoodLifeTheme
import com.forknowledge.feature.planner.PlannerRoute
import com.forknowledge.foodlife.ui.AppScreen
import com.forknowledge.foodlife.ui.rememberAppState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var networkManager: NetworkManager

    private val viewModel by viewModels<MainViewModel>()

    private var isLoading = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val splash = installSplashScreen()
        splash.setKeepOnScreenCondition { isLoading }

        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.light(
                Color.TRANSPARENT,
                Color.TRANSPARENT
            )
        )

        viewModel.isLoading.observe(this) { isLoading = it }

        setContent {
            FoodLifeTheme {
                AppScreen(
                    appState = rememberAppState(networkManager = networkManager),
                    startDestinationRoute = PlannerRoute
                )
            }
        }
        /*viewModel.startDestinationRoute.observe(this) { route ->
            route?.let {
                setContent {
                    FoodLifeTheme {
                        AppScreen(
                            appState = rememberAppState(networkManager = networkManager),
                            startDestinationRoute = route
                        )
                    }
                }
            }
        }*/
    }
}
