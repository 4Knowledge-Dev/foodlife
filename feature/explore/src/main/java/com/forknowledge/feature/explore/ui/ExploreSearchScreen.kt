package com.forknowledge.feature.explore.ui

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil3.compose.AsyncImage
import com.forknowledge.core.ui.R.drawable
import com.forknowledge.core.ui.theme.Black374957
import com.forknowledge.core.ui.theme.Green86BF3E
import com.forknowledge.core.ui.theme.Typography
import com.forknowledge.core.ui.theme.component.AppButtonSmall
import com.forknowledge.core.ui.theme.component.AppButtonSmallLoading
import com.forknowledge.core.ui.theme.component.AppSnackBar
import com.forknowledge.core.ui.theme.component.AppText
import com.forknowledge.core.ui.theme.component.AppTextField
import com.forknowledge.core.ui.theme.component.LoadingIndicator
import com.forknowledge.core.ui.theme.state.SnackBarState
import com.forknowledge.feature.explore.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreSearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    isAddMealPlanProcess: Boolean,
    mealPosition: Int,
    dateInMillis: Long,
    onNavigateToMealPlan: () -> Unit,
    onNavigateToRecipeDetail: (Int) -> Unit,
    onNavigateBack: () -> Unit
) {
    // val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val snackbarHostState = remember { SnackbarHostState() }

    val isLoading = viewModel.isLoading
    val shouldShowLoadingButton = viewModel.shouldShowLoadingButton
    val shouldShowError = viewModel.shouldShowError
    val onNavigateToMealPlan = viewModel.onNavigateToMealPlan
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val recipes = viewModel.recipes.collectAsLazyPagingItems()
    val selectedRecipes by viewModel.selectedRecipes.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        // focusRequester.requestFocus()
    }

    if (shouldShowError) {
        val message =
            stringResource(R.string.explore_search_adding_to_meal_plan_snackbar_error_message)
        LaunchedEffect(Unit) {
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Long
            )
        }
    }

    if (onNavigateToMealPlan) {
        onNavigateToMealPlan()
    }

    Scaffold(
        topBar = {
            if (selectedRecipes.isEmpty()) {
                SearchAppBar(
                    // modifier = Modifier.focusRequester(focusRequester),
                    query = searchQuery,
                    onQueryChange = { viewModel.updateSearchQuery(it) },
                    onSearch = {
                        focusManager.clearFocus()
                        viewModel.search(it)
                    },
                    onBack = onNavigateBack
                )
            } else {
                ActionAppBar(onCloseClicked = { viewModel.clearSelectedRecipes() })
            }
        },
        snackbarHost = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                SnackbarHost(
                    hostState = snackbarHostState,
                    snackbar = {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            AppSnackBar(
                                message = it.visuals.message,
                                state = SnackBarState.FAILURE
                            )
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                LoadingIndicator()
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(White)
            ) {
                LazyVerticalGrid(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(
                        vertical = 24.dp,
                        horizontal = 10.dp
                    ),
                    horizontalArrangement = Arrangement.Center
                ) {
                    items(
                        count = recipes.itemCount,
                        key = recipes.itemKey { it.id }
                    ) { index ->
                        recipes[index]?.let { recipe ->
                            RecipeItem(
                                name = recipe.name,
                                imageUrl = recipe.imageUrl,
                                cookTime = recipe.cookTime,
                                isSelected = selectedRecipes.contains(recipe),
                                onItemClick = {
                                    if (isAddMealPlanProcess) {
                                        viewModel.updateSelectedRecipes(recipe)
                                    } else {
                                        onNavigateToRecipeDetail(recipe.id)
                                    }
                                }
                            )
                        }
                    }
                }

                if (selectedRecipes.isNotEmpty()) {
                    if (shouldShowLoadingButton) {
                        AppButtonSmallLoading(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 24.dp),
                            text = stringResource(R.string.explore_search_adding_to_meal_plan_button)
                        )
                    } else {
                        AppButtonSmall(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 24.dp),
                            text = stringResource(R.string.explore_search_add_to_meal_plan_button),
                            trailingIcon = drawable.ic_add,
                            onClicked = { viewModel.addToMealPlan(dateInMillis, mealPosition) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SearchAppBar(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    onBack: () -> Unit
) {
    AppTextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                top = 24.dp,
                start = 16.dp,
                end = 16.dp
            ),
        value = query,
        placeholder = stringResource(R.string.explore_search_bar_placeholder),
        leadingIcon = drawable.ic_arrow_previous,
        onLeadingIconClick = { onBack() },
        corner = 32.dp,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = { onSearch(query) }
        ),
        onValueChanged = { onQueryChange(it) },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionAppBar(
    onCloseClicked: () -> Unit
) {
    TopAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(MaterialTheme.colorScheme.background),
        title = {},
        actions = {
            Icon(
                modifier = Modifier
                    .padding(
                        top = 24.dp,
                        bottom = 24.dp,
                        end = 16.dp
                    )
                    .size(32.dp)
                    .clickable { onCloseClicked() },
                painter = painterResource(id = drawable.ic_delete),
                contentDescription = null,
                tint = Black374957
            )
        }
    )
}

@Composable
fun RecipeItem(
    name: String,
    imageUrl: String,
    cookTime: Int,
    isSelected: Boolean,
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
            model = imageUrl,
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
                    Brush.verticalGradient(
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
                    R.plurals.explore_recipe_cook_time_text, cookTime, cookTime
                ), textStyle = Typography.bodyMedium, color = White
            )

            Spacer(modifier = Modifier.weight(1f))
        }

        if (isSelected) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Black374957.copy(alpha = 0.4f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(32.dp),
                    painter = painterResource(id = drawable.ic_complete_solid),
                    contentDescription = null,
                    tint = Green86BF3E
                )
            }
        }
    }
}

@Preview
@Composable
fun SearchAppBarPreview() {
    SearchAppBar(
        query = "",
        onQueryChange = {},
        onSearch = {},
        onBack = {}
    )
}

@Preview
@Composable
fun ActionAppBarPreview() {
    ActionAppBar {}
}

@Preview
@Composable
fun RecipeItemPreview() {
    RecipeItem(
        name = "Spaghetti Bolognese ".repeat(3),
        imageUrl = "",
        cookTime = 30,
        isSelected = true,
        onItemClick = {}
    )
}
