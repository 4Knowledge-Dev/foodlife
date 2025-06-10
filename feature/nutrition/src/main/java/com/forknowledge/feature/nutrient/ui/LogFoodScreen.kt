package com.forknowledge.feature.nutrient.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.forknowledge.core.ui.R.drawable
import com.forknowledge.core.ui.theme.Black374957
import com.forknowledge.core.ui.theme.Green86BF3E
import com.forknowledge.core.ui.theme.GreenA1CE50
import com.forknowledge.core.ui.theme.GreyEBEBEB
import com.forknowledge.core.ui.theme.GreyF4F5F5
import com.forknowledge.core.ui.theme.Typography
import com.forknowledge.core.ui.theme.component.AppText
import com.forknowledge.feature.model.Recipe
import com.forknowledge.feature.model.logRecipes
import com.forknowledge.feature.nutrient.R
import com.forknowledge.feature.nutrient.RecipeItem
import com.forknowledge.feature.nutrient.toMealName
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
data class LogFoodRoute(
    val meal: Long,
    val hasLoggedRecipe: Boolean,
    val dateInMillis: Long
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogFoodScreen(
    meal: Long,
    hasLoggedFood: Boolean,
    dateInMillis: Long,
    onNavigateToSearch: (Long, Boolean, Long) -> Unit,
    onNavigateBack: () -> Unit,
) {
    var selectedTabIndex by remember {
        mutableIntStateOf(LogFoodTab.FREQUENT.ordinal)
    }

    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { LogFoodTab.entries.size })

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            selectedTabIndex = page
        }
    }

    Scaffold(
        topBar = {
            LogFoodTopAppBar(
                meal = stringResource(meal.toMealName()),
                selectedTabIndex = selectedTabIndex,
                onTabChanged = {
                    selectedTabIndex = it
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(it)
                    }
                },
                onNavigateToSearch = { onNavigateToSearch(meal, hasLoggedFood, dateInMillis) },
                onNavigateBack = onNavigateBack
            )
        },
    ) { innerPadding ->

        HorizontalPager(
            modifier = Modifier.padding(innerPadding),
            state = pagerState
        ) { pageNumber ->
            ContentSection(
                recipes = when (selectedTabIndex) {
                    LogFoodTab.FREQUENT.ordinal -> logRecipes
                    LogFoodTab.PLAN.ordinal -> logRecipes
                    else -> logRecipes
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogFoodTopAppBar(
    meal: String,
    selectedTabIndex: Int,
    onTabChanged: (Int) -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .background(color = White)
            .padding(
                top = 24.dp,
                start = 24.dp,
                end = 24.dp
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.clickable { onNavigateBack() },
                painter = painterResource(id = drawable.ic_back),
                tint = Black374957,
                contentDescription = null
            )

            AppText(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 24.dp),
                text = meal,
                textStyle = Typography.titleSmall,
                textAlign = TextAlign.Center,
            )
        }

        // SearchBar
        Row(
            modifier = Modifier
                .padding(top = 24.dp)
                .height(55.dp)
                .background(
                    color = GreyF4F5F5,
                    shape = RoundedCornerShape(32.dp)
                )
                .clip(RoundedCornerShape(32.dp))
                .clickable { onNavigateToSearch() }
                .padding(
                    vertical = 16.dp,
                    horizontal = 21.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(id = drawable.ic_search),
                tint = Black374957,
                contentDescription = null
            )

            AppText(
                modifier = Modifier.padding(start = 18.dp),
                text = stringResource(R.string.nutrient_log_food_search_bar_label),
                textStyle = Typography.bodyMedium,
                color = Black374957
            )

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(id = drawable.ic_scan),
                tint = Black374957,
                contentDescription = null
            )
        }

        PrimaryTabRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            selectedTabIndex = selectedTabIndex,
            containerColor = White,
            indicator = {
                TabRowDefaults.PrimaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(
                        selectedTabIndex,
                        matchContentSize = true
                    ),
                    width = 70.dp,
                    color = GreenA1CE50,
                )
            }
        ) {
            LogFoodTab.entries.forEachIndexed { index, tab ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { onTabChanged(index) },
                    text = {
                        AppText(
                            text = stringResource(tab.label),
                            textStyle = Typography.labelMedium
                        )
                    },
                    selectedContentColor = Green86BF3E,
                )
            }
        }
    }
}

@Composable
fun ContentSection(
    recipes: List<Recipe>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .background(White),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        itemsIndexed(recipes) { index, recipe ->
            RecipeItem(false, recipe = recipe) {}
            if (index < logRecipes.lastIndex) {
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    color = GreyEBEBEB
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun LogFoodTopAppBarPreview() {
    LogFoodTopAppBar(
        meal = "Breakfast",
        selectedTabIndex = 0,
        onTabChanged = {},
        onNavigateToSearch = {},
        onNavigateBack = {}
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun ContentSectionPreview() {
    ContentSection(recipes = logRecipes)
}
