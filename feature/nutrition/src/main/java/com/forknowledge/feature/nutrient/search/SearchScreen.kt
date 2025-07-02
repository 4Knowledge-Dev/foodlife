package com.forknowledge.feature.nutrient.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.forknowledge.core.common.extension.toFirestoreDateTime
import com.forknowledge.core.ui.R.drawable
import com.forknowledge.core.ui.theme.Black063336
import com.forknowledge.core.ui.theme.Black374957
import com.forknowledge.core.ui.theme.Grey7F000000
import com.forknowledge.core.ui.theme.Grey808993
import com.forknowledge.core.ui.theme.Grey8A949F
import com.forknowledge.core.ui.theme.GreyDADADA
import com.forknowledge.core.ui.theme.Typography
import com.forknowledge.core.ui.theme.component.AppButtonSmall
import com.forknowledge.core.ui.theme.component.AppText
import com.forknowledge.core.ui.theme.component.ErrorMessage
import com.forknowledge.core.ui.theme.component.LoadingIndicator
import com.forknowledge.feature.model.Nutrient
import com.forknowledge.feature.model.NutritionSearchRecipe
import com.forknowledge.feature.nutrient.R
import com.forknowledge.feature.nutrient.Utils
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filter
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    mealPosition: Int,
    dateInMillis: Long,
    onNavigateBack: () -> Unit,
    onNavigateToNutrient: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val isLoading = viewModel.isLoading
    val shouldShowItemProcessLoading = viewModel.shouldShowItemProcessLoading
    val shouldShowError = viewModel.shouldShowError
    val onProcessItemId = viewModel.onProcessItemId
    val logRecipeResult = viewModel.logRecipeResult
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val recipes = viewModel.recipes.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        SearchBar(
            modifier = Modifier.align(Alignment.TopCenter),
            inputField = {
                SearchBarDefaults.InputField(
                    modifier = Modifier.focusRequester(focusRequester),
                    query = searchQuery,
                    onQueryChange = { viewModel.updateSearchQuery(it) },
                    onSearch = {
                        focusManager.clearFocus()
                        viewModel.search(it)
                    },
                    expanded = true,
                    onExpandedChange = { },
                    placeholder = {
                        AppText(
                            text = stringResource(R.string.nutrient_log_food_search_screen_search_bar_label),
                            color = Grey8A949F
                        )
                    },
                    leadingIcon = {
                        Icon(
                            modifier = Modifier.clickable { onNavigateBack() },
                            painter = painterResource(id = drawable.ic_arrow_previous),
                            tint = Black374957,
                            contentDescription = null
                        )
                    }
                )
            },
            expanded = true,
            colors = SearchBarDefaults.colors(
                containerColor = White
            ),
            onExpandedChange = { },
        ) {
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    LoadingIndicator()
                }
            }

            if (shouldShowError) {
                ErrorMessage(stringResource(R.string.nutrient_insights_error_message))
            }

            if (!isLoading && !shouldShowError) {
                if (recipes.itemCount > 0) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background),
                        contentPadding = PaddingValues(
                            vertical = 8.dp,
                            horizontal = 16.dp
                        )
                    ) {
                        items(
                            count = recipes.itemCount,
                            key = recipes.itemKey { it.id }
                        ) { index ->
                            recipes[index]?.let {
                                RecipeItem(
                                    recipe = it,
                                    showLoading = shouldShowItemProcessLoading && it.id == onProcessItemId,
                                    logRecipeResult = logRecipeResult,
                                    onLogRecipe = {
                                        viewModel.logRecipe(
                                            date = dateInMillis.toFirestoreDateTime(),
                                            mealPosition = mealPosition,
                                            recipe = it
                                        )
                                    }
                                )
                            }
                        }
                    }
                } else {
                    if (searchQuery.isNotEmpty()) {
                        NoDataFoundMessage()
                    }
                }
            }
        }

        if (!isLoading && recipes.itemCount > 0) {
            AppButtonSmall(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 24.dp),
                text = stringResource(R.string.nutrient_log_food_search_screen_button_done),
                onClicked = onNavigateToNutrient
            )
        }
    }
}


@Composable
fun RecipeItem(
    recipe: NutritionSearchRecipe,
    showLoading: Boolean,
    logRecipeResult: Utils,
    onLogRecipe: () -> Unit
) {
    val successComposition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.anim_success)
    )
    val failComposition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.anim_fail)
    )
    var showAnim by remember { mutableStateOf(false) }
    val successProgress by animateLottieCompositionAsState(
        composition = successComposition,
        isPlaying = showAnim
    )
    val failProgress by animateLottieCompositionAsState(
        composition = failComposition,
        isPlaying = showAnim
    )

    LaunchedEffect(showLoading) {
        snapshotFlow { showLoading }
            .filter { !it }
            .collect { isLoading ->
                if (logRecipeResult != Utils.NONE) {
                    showAnim = true
                    delay(1500)
                    showAnim = false
                }
            }
    }

    ConstraintLayout(
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth()
            .graphicsLayer {
                shape = RoundedCornerShape(
                    topStart = 8.dp,
                    topEnd = 8.dp,
                    bottomStart = 8.dp,
                    bottomEnd = 8.dp
                )
                shadowElevation = 2.dp.toPx()
                spotShadowColor = Black063336
                clip = true
            }
            .background(
                color = White,
                shape = RoundedCornerShape(8.dp)
            )
            .clip(RoundedCornerShape(8.dp))
            .clickable {}
            .padding(
                vertical = 12.dp,
                horizontal = 16.dp
            )
    ) {
        val (recipeImage, icon, recipeName, recipeAmount, divider, carbs, protein, fat) = createRefs()

        Image(
            modifier = Modifier
                .size(60.dp)
                .background(
                    color = Color.Unspecified,
                    shape = CircleShape
                )
                .clip(CircleShape)
                .constrainAs(recipeImage) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                },
            painter = painterResource(drawable.img_sample),
            contentScale = ContentScale.Crop,
            contentDescription = null
        )

        AppText(
            modifier = Modifier.constrainAs(recipeName) {
                top.linkTo(recipeImage.top)
                bottom.linkTo(recipeAmount.top)
                start.linkTo(recipeImage.end, margin = 16.dp)
                end.linkTo(icon.start)
                horizontalBias = 0F
            },
            text = recipe.name,
            textStyle = Typography.labelMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        AppText(
            modifier = Modifier.constrainAs(recipeAmount) {
                top.linkTo(recipeName.bottom)
                bottom.linkTo(recipeImage.bottom)
                start.linkTo(recipeImage.end, margin = 16.dp)
                end.linkTo(icon.start)
                horizontalBias = 0F
            },
            text = pluralStringResource(
                id = R.plurals.nutrient_log_food_item_amount,
                count = recipe.servings,
                recipe.nutrients[0].amount.roundToInt(),
                recipe.servings
            ),
            textStyle = Typography.bodySmall,
            color = Grey808993
        )

        Box(
            modifier = Modifier
                .size(28.dp)
                .constrainAs(icon) {
                    top.linkTo(recipeImage.top)
                    bottom.linkTo(recipeImage.bottom)
                    end.linkTo(parent.end, margin = 16.dp)
                }
        ) {
            if (!showLoading && !showAnim) {
                Icon(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { onLogRecipe() },
                    painter = painterResource(drawable.ic_add_solid),
                    tint = Black374957,
                    contentDescription = null
                )
            }
            if (showLoading) {
                LoadingIndicator(
                    modifier = Modifier.fillMaxSize(),
                    strokeWidth = 3.dp
                )
            }
            if (showAnim) {
                when (logRecipeResult) {
                    Utils.NONE -> { /* Do nothing */
                    }

                    Utils.SUCCESS -> {
                        LottieAnimation(
                            modifier = Modifier.fillMaxSize(),
                            composition = successComposition,
                            progress = { successProgress },
                        )
                    }

                    Utils.FAIL -> {
                        LottieAnimation(
                            modifier = Modifier.fillMaxSize(),
                            composition = failComposition,
                            progress = { failProgress },
                        )
                    }
                }
            }
        }

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(divider) {
                    top.linkTo(recipeImage.bottom, margin = 16.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            color = GreyDADADA
        )

        Column(
            modifier = Modifier.constrainAs(carbs) {
                top.linkTo(divider.bottom, margin = 16.dp)
                start.linkTo(parent.start)
                end.linkTo(protein.start, margin = 24.dp)
            },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AppText(
                text = stringResource(
                    R.string.nutrient_log_food_search_screen_nutrient_amount,
                    recipe.nutrients[1].amount
                ),
                textStyle = Typography.labelSmall
            )

            AppText(
                text = stringResource(R.string.nutrient_label_carb),
                textStyle = Typography.bodySmall,
                color = Grey808993
            )
        }

        Column(
            modifier = Modifier.constrainAs(protein) {
                top.linkTo(divider.bottom, margin = 16.dp)
                start.linkTo(carbs.end)
                end.linkTo(fat.start)
            },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AppText(
                text = stringResource(
                    R.string.nutrient_log_food_search_screen_nutrient_amount,
                    recipe.nutrients[2].amount
                ),
                textStyle = Typography.labelSmall
            )

            AppText(
                text = stringResource(R.string.nutrient_label_protein),
                textStyle = Typography.bodySmall,
                color = Grey808993
            )
        }

        Column(
            modifier = Modifier.constrainAs(fat) {
                top.linkTo(divider.bottom, margin = 16.dp)
                start.linkTo(protein.end, margin = 24.dp)
                end.linkTo(parent.end)
            },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AppText(
                text = stringResource(
                    R.string.nutrient_log_food_search_screen_nutrient_amount,
                    recipe.nutrients[3].amount
                ),
                textStyle = Typography.labelSmall
            )

            AppText(
                text = stringResource(R.string.nutrient_label_fat),
                textStyle = Typography.bodySmall,
                color = Grey808993
            )
        }
    }
}

@Composable
fun NoDataFoundMessage() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.size(250.dp),
                painter = painterResource(drawable.img_vector_no_data_found),
                contentDescription = null
            )

            AppText(
                text = stringResource(R.string.nutrient_log_food_search_screen_no_data_found),
                textStyle = Typography.bodyLarge,
                color = Grey7F000000
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun RecipeItemPreview() {
    RecipeItem(
        recipe = NutritionSearchRecipe(
            id = 10,
            name = "Spaghetti Bolognese",
            imageUrl = "",
            servings = 2,
            nutrients = List(4) {
                Nutrient(
                    name = "Calories",
                    amount = 90f,
                    unit = "kcal",
                    dailyPercentValue = 35
                )
            }
        ),
        showLoading = false,
        logRecipeResult = Utils.NONE,
        onLogRecipe = {}
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun NoDataFoundMessagePreview() {
    NoDataFoundMessage()
}
