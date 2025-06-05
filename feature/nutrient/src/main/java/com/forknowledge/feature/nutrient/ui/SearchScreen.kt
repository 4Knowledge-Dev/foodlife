package com.forknowledge.feature.nutrient.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.forknowledge.core.ui.R.drawable
import com.forknowledge.core.ui.theme.Black374957
import com.forknowledge.core.ui.theme.Grey8A949F
import com.forknowledge.core.ui.theme.component.AppText
import com.forknowledge.feature.model.logRecipes
import com.forknowledge.feature.nutrient.R
import kotlinx.serialization.Serializable

@Serializable
object SearchRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarSection(
    onNavigateBack: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

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
                    onQueryChange = { searchQuery = it },
                    onSearch = {
                        focusManager.clearFocus()
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
            ContentSection(
                recipes = logRecipes
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun SearchScreenPreview() {
    SearchBarSection {}
}
