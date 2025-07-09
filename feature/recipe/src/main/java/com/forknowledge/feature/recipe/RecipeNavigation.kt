package com.forknowledge.feature.recipe

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.navOptions
import kotlinx.serialization.Serializable

@Serializable
data object SavedRecipeRoute

@Serializable
data class RecipeRoute(
    val dateInMillis: Long = 0,
    val mealPosition: Int = 0,
    val isSavedRecipe: Boolean = false,
    val recipeId: Int
)

@Serializable
data object NutritionImpactRoute

@Serializable
data object CreateRecipeRoute

fun NavController.navigateToSavedRecipe(navOptions: NavOptions? = null) {
    navigate(SavedRecipeRoute, navOptions)
}

fun NavController.navigateToSavedRecipeDetail(recipeRoute: RecipeRoute) {
    val navOptions = navOptions {
        popUpTo(SavedRecipeRoute)
    }
    navigate(recipeRoute, navOptions)
}