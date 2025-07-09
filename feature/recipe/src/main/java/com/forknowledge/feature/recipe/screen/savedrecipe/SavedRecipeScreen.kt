package com.forknowledge.feature.recipe.screen.savedrecipe

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.forknowledge.core.ui.R.drawable
import com.forknowledge.core.ui.theme.Typography
import com.forknowledge.core.ui.theme.component.AppFloatingActionButton
import com.forknowledge.core.ui.theme.component.AppText
import com.forknowledge.core.ui.theme.component.ErrorMessage
import com.forknowledge.core.ui.theme.component.LoadingIndicator
import com.forknowledge.feature.recipe.R

@Composable
fun SavedRecipeScreen(
    viewModel: SavedRecipeViewModel = hiltViewModel(),
    onNavigateToCreateRecipe: () -> Unit,
    onNavigateToRecipeDetail: (Int) -> Unit
) {
    val recipes by viewModel.recipes.collectAsStateWithLifecycle()
    val shouldShowLoading = viewModel.shouldShowLoading
    val shouldShowError = viewModel.shouldShowError

    Scaffold(
        topBar = {
            if (!shouldShowLoading && !shouldShowError) {
                AppText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp, horizontal = 16.dp),
                    text = stringResource(R.string.saved_recipe_title),
                    textStyle = Typography.titleMedium
                )
            }
        },
        floatingActionButton = {
            if (!shouldShowLoading && !shouldShowError) {
                AppFloatingActionButton { onNavigateToCreateRecipe() }
            }
        }
    ) { innerPadding ->
        if (shouldShowLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LoadingIndicator()
            }
        }

        if (shouldShowError) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                ErrorMessage(stringResource(R.string.saved_recipe_get_recipe_error))
            }
        }

        if (!shouldShowLoading && !shouldShowError) {
            LazyVerticalGrid(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(
                    vertical = 24.dp,
                    horizontal = 16.dp
                ),
                horizontalArrangement = Arrangement.Center
            ) {
                items(recipes) { recipe ->
                    SavedRecipeItem(
                        name = recipe.recipeName,
                        imageUrl = recipe.imageUrl,
                        cookTime = recipe.readyInMinutes,
                        onItemClick = { onNavigateToRecipeDetail(recipe.recipeId) }
                    )
                }
            }
        }
    }
}

@Composable
fun SavedRecipeItem(
    name: String,
    imageUrl: String,
    cookTime: Int,
    onItemClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(
                top = 12.dp, start = 6.dp, end = 6.dp
            )
            .size(
                width = 160.dp, height = 190.dp
            )
            .background(
                color = Color.Unspecified, shape = RoundedCornerShape(8.dp)
            )
            .clip(RoundedCornerShape(8.dp))
            .clickable { onItemClick() }, contentAlignment = Alignment.BottomCenter
    ) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = "https://assets.epicurious.com/photos/5988e3458e3ab375fe3c0caf/1:1/w_2240,c_limit/How-to-Make-Chicken-Alfredo-Pasta-hero-02082017.jpg",
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = drawable.img_vector_loading),
            error = painterResource(id = drawable.img_vector_loading),
            contentDescription = null,
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .background(
                    brush = Brush.verticalGradient(
                        0.0f to Color.Transparent,
                        0.3f to Color(0x4D1A1A1A),
                        0.9f to Color(0xBF101010),
                        1.0f to Color(0xFF222222)
                    )
                )
                .padding(horizontal = 8.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))

            AppText(
                modifier = Modifier.fillMaxWidth(),
                text = name,
                textStyle = Typography.labelLarge,
                color = White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )

            AppText(
                modifier = Modifier.padding(top = 8.dp), text = pluralStringResource(
                    R.plurals.saved_recipe_item_cook_time_text,
                    cookTime,
                    cookTime
                ),
                textStyle = Typography.bodyLarge,
                color = White
            )

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Preview
@Composable
fun SavedRecipeItemPreview() {
    SavedRecipeItem(
        name = "Spaghetti Bolognese ".repeat(3),
        imageUrl = "https://img.spoonacular.com/recipes/1697885-556x370.jpg",
        cookTime = 30,
        onItemClick = {}
    )
}