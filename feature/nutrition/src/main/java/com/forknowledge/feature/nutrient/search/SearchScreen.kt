package com.forknowledge.feature.nutrient.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.forknowledge.core.ui.R.drawable
import com.forknowledge.core.ui.theme.Black374957
import com.forknowledge.core.ui.theme.Grey8A949F
import com.forknowledge.core.ui.theme.GreyEBEBEB
import com.forknowledge.core.ui.theme.component.AppText
import com.forknowledge.core.ui.theme.component.LoadingIndicator
import com.forknowledge.feature.nutrient.R
import com.forknowledge.feature.nutrient.RecipeItem
import kotlinx.serialization.Serializable

@Serializable
object SearchRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val isLoading = viewModel.isLoading
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val recipes = viewModel.recipes.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
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
                LoadingIndicator()
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    items(
                        count = recipes.itemCount,
                        key = recipes.itemKey { it.id }
                    ) { index ->
                        recipes[index]?.let {
                            RecipeItem(recipe = it)
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 24.dp),
                                color = GreyEBEBEB
                            )
                        }
                    }
                }
            }
        }
    }
}
