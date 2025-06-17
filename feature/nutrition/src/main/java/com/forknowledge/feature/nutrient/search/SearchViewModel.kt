package com.forknowledge.feature.nutrient.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.forknowledge.core.common.AppConstant.SEARCH_DEBOUNCE
import com.forknowledge.core.common.Result
import com.forknowledge.core.common.asFlowResult
import com.forknowledge.core.data.FoodRepository
import com.forknowledge.core.data.UserRepository
import com.forknowledge.feature.model.NutritionSearchRecipe
import com.forknowledge.feature.nutrient.LogRecipeState
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
import java.util.Date
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val foodRepository: FoodRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow<String>("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _recipes = MutableStateFlow<PagingData<NutritionSearchRecipe>>(PagingData.empty())
    val recipes: StateFlow<PagingData<NutritionSearchRecipe>> = _recipes

    var logRecipes by mutableStateOf(listOf<NutritionSearchRecipe>())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var shouldShowItemProcessLoading by mutableStateOf(false)
        private set

    var onProcessItemId by mutableIntStateOf(0)
        private set

    var logRecipeResult by mutableStateOf<LogRecipeState>(LogRecipeState.NONE)
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
                    .searchRecipeForNutrition(
                        query = query,
                        includeNutrition = true
                    )
            }
            .asFlowResult()
            .onEach { result ->
                logRecipeResult = LogRecipeState.NONE
                if (_searchQuery.value.isNotEmpty()) {
                    when (result) {
                        is Result.Loading -> isLoading = true
                        is Result.Success -> {
                            isLoading = false
                            _recipes.update { result.data }
                        }

                        is Result.Error -> isLoading = false
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.update { query }
    }

    fun search(query: String) {
        logRecipeResult = LogRecipeState.NONE
        viewModelScope.launch {
            foodRepository
                .searchRecipeForNutrition(
                    query = query,
                    includeNutrition = true
                )
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

    fun logRecipe(
        date: Date,
        mealPosition: Int,
        recipe: NutritionSearchRecipe
    ) {
        shouldShowItemProcessLoading = true
        onProcessItemId = recipe.id

        viewModelScope.launch {
            when (userRepository.updateRecipeList(
                date = date,
                mealPosition = mealPosition,
                recipe = recipe
            )) {
                is Result.Loading -> Unit
                is Result.Success -> {
                    shouldShowItemProcessLoading = false
                    onProcessItemId = 0
                    logRecipeResult = LogRecipeState.SUCCESS
                }

                is Result.Error -> {
                    shouldShowItemProcessLoading = false
                    onProcessItemId = 0
                    logRecipeResult = LogRecipeState.FAIL
                }
            }
        }
    }

}
