package com.forknowledge.feature.recipe.screen.savedrecipe

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.forknowledge.core.ui.theme.component.AppFloatingActionButton

@Composable
fun SavedRecipeScreen(
    onNavigateToCreateRecipe: () -> Unit
) {
    Scaffold(
        floatingActionButton = { AppFloatingActionButton { onNavigateToCreateRecipe() } }
    ) { innerPadding ->
        innerPadding
    }
}