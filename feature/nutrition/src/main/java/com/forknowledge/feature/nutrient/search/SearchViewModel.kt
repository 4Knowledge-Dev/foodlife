package com.forknowledge.feature.nutrient.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.forknowledge.core.common.Result
import com.forknowledge.core.common.asFlowResult
import com.forknowledge.core.data.FoodRepository
import com.forknowledge.feature.model.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

const val SEARCH_DEBOUNCE = 300L

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val foodRepository: FoodRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow<String>("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _recipes = MutableStateFlow<PagingData<Recipe>>(PagingData.empty())
    val recipes: StateFlow<PagingData<Recipe>> = _recipes

    var isLoading by mutableStateOf(false)
        private set

    init {
        viewModelScope.launch { observeQueryChanges() }
    }

    private fun observeQueryChanges() {
        _searchQuery
            .debounce(SEARCH_DEBOUNCE)
            .filter { it.isNotBlank() }
            .flatMapLatest { query ->
                foodRepository
                    .searchRecipe(query)
                    .asFlowResult()
            }
            .onEach { result ->
                when (result) {
                    is Result.Loading -> isLoading = true
                    is Result.Success -> {
                        _recipes.update { result.data }
                        isLoading = false
                    }
                    is Result.Error -> isLoading = false
                }
            }
            .launchIn(viewModelScope)
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.update { query }
    }

    fun search(query: String) {
        viewModelScope.launch {
            foodRepository
                .searchRecipe(query)
                .asFlowResult()
                .collect { result ->
                    when (result) {
                        is Result.Loading -> isLoading = true
                        is Result.Success -> {
                            _recipes.update { result.data }
                            isLoading = false
                        }

                        is Result.Error -> isLoading = false
                    }
                }
        }
    }
}
