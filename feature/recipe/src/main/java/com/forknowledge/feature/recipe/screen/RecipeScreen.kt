package com.forknowledge.feature.recipe.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.forknowledge.core.ui.R.drawable
import com.forknowledge.core.ui.theme.Black374957
import com.forknowledge.core.ui.theme.GreenA1CE50
import com.forknowledge.core.ui.theme.Typography
import com.forknowledge.core.ui.theme.component.AppText
import com.forknowledge.feature.model.Recipe
import com.forknowledge.feature.recipe.RecipeTab
import com.forknowledge.feature.recipe.RecipeViewModel
import kotlinx.coroutines.launch

@Composable
fun RecipeScreen(
    viewModel: RecipeViewModel = hiltViewModel(),
    recipeId: Int,
    onNavigateBack: () -> Unit
) {
    val recipe by viewModel.recipe.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getRecipe(recipeId)
    }

    Scaffold(
        topBar = { RecipeTopBar(onNavigateBack) }
    ) { innerPadding ->
        if (recipe != null) {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                BackLayerSection(
                    recipeImageUrl = recipe!!.imageUrl,
                    recipeName = recipe!!.recipeName
                )
                FrontLayerSection(recipe = recipe!!)
            }
        }
    }
}

@Composable
fun RecipeTopBar(
    onNavigateBack: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = { onNavigateBack() },
        ) {
            Icon(
                painter = painterResource(id = drawable.ic_back),
                tint = Black374957,
                contentDescription = null
            )
        }

        IconButton(
            onClick = { /*TODO*/ },
        ) {
            Icon(
                painter = painterResource(id = drawable.ic_options),
                tint = Black374957,
                contentDescription = null
            )
        }
    }
}

@Composable
fun BackLayerSection(
    recipeImageUrl: String,
    recipeName: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = White,
                shape = RoundedCornerShape(
                    bottomStart = 24.dp,
                    bottomEnd = 24.dp
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp),
            model = recipeImageUrl,
            placeholder = painterResource(id = drawable.img_sample),
            contentScale = ContentScale.Crop,
            contentDescription = null,
        )

        AppText(
            modifier = Modifier
                .padding(vertical = 24.dp)
                .fillMaxWidth(),
            text = recipeName,
            textStyle = Typography.titleSmall,
            textAlign = TextAlign.Center
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FrontLayerSection(
    recipe: Recipe
) {
    var selectedTabIndex by remember { mutableIntStateOf(RecipeTab.INGREDIENTS.ordinal) }
    val pagerState = rememberPagerState(pageCount = { RecipeTab.entries.size })
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            selectedTabIndex = page
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = White,
                shape = RoundedCornerShape(
                    topStart = 24.dp,
                    topEnd = 24.dp
                )
            )
            .clip(
                RoundedCornerShape(
                    topStart = 24.dp,
                    topEnd = 24.dp
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SecondaryTabRow(
            modifier = Modifier
                .fillMaxWidth(),
            selectedTabIndex = selectedTabIndex,
            containerColor = White,
            indicator = {
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(
                        selectedTabIndex,
                        matchContentSize = true
                    ),
                    height = 2.dp,
                    color = GreenA1CE50,
                )
            }
        ) {
            RecipeTab.entries.forEachIndexed { index, tab ->
                Tab(
                    selected = selectedTabIndex == index,
                    text = {
                        AppText(
                            text = stringResource(tab.title),
                            textStyle = Typography.labelMedium
                        )
                    },
                    onClick = {
                        selectedTabIndex = index
                        coroutineScope.launch { pagerState.animateScrollToPage(index) }
                    },
                )
            }
        }

        HorizontalPager(
            modifier = Modifier.fillMaxWidth(),
            state = pagerState,
            beyondViewportPageCount = 2,
            verticalAlignment = Alignment.Top
        ) { pageIndex ->
            when (pageIndex) {
                RecipeTab.INGREDIENTS.ordinal -> {
                    IngredientTabContent(
                        summary = recipe.summary,
                        originalServings = recipe.servings,
                        ingredients = recipe.ingredients
                    )
                }

                RecipeTab.INSTRUCTIONS.ordinal -> {
                    InstructionTabContent(
                        sourceUrl = recipe.sourceUrl,
                        readyInMinutes = recipe.readyInMinutes,
                        prepTime = recipe.preparationMinutes,
                        cookTime = recipe.cookingMinutes,
                        steps = recipe.steps
                    )
                }

                RecipeTab.HEALTH.ordinal -> {}
            }
        }
    }
}

@Preview
@Composable
fun RecipeTopBarPreview() {
    RecipeTopBar(
        onNavigateBack = {}
    )
}

@Preview
@Composable
fun BackLayerSectionPreview() {
    BackLayerSection(
        recipeImageUrl = "",
        recipeName = "Recipe Name"
    )
}

/*@Preview
@Composable
fun FrontLayerSectionPreview() {
    FrontLayerSection(
        recipe = Recipe()
    )
}*/
