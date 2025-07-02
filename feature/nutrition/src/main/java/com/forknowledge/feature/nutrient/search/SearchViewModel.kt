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
import com.forknowledge.core.common.extension.toFirestoreDateTime
import com.forknowledge.core.data.FoodRepository
import com.forknowledge.core.data.UserRepository
import com.forknowledge.feature.model.NutritionSearchRecipe
import com.forknowledge.feature.nutrient.Utils
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

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val foodRepository: FoodRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _recipes = MutableStateFlow<PagingData<NutritionSearchRecipe>>(PagingData.empty())
    val recipes: StateFlow<PagingData<NutritionSearchRecipe>> = _recipes

    var isLoading by mutableStateOf(false)
        private set

    var shouldShowItemProcessLoading by mutableStateOf(false)
        private set

    var shouldShowError by mutableStateOf(false)
        private set

    var onProcessItemId by mutableIntStateOf(0)
        private set

    var logRecipeResult by mutableStateOf(Utils.NONE)
        private set

    init {
        viewModelScope.launch { observeQueryChanges() }
    }

    private fun observeQueryChanges() {
        _searchQuery
            .debounce(SEARCH_DEBOUNCE)
            .filter {
                isLoading = false
                it.isNotBlank()
            }
            .flatMapLatest { query ->
                foodRepository.searchRecipeForNutrition(query = query)
            }
            .asFlowResult()
            .onEach { result ->
                shouldShowError = false
                logRecipeResult = Utils.NONE
                if (_searchQuery.value.isNotEmpty()) {
                    when (result) {
                        is Result.Loading -> isLoading = true
                        is Result.Success -> {
                            isLoading = false
                            _recipes.update { result.data }
                        }

                        is Result.Error -> {
                            isLoading = false
                            shouldShowError = true
                        }
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun updateSearchQuery(query: String) {
        if (query.isNotEmpty()) {
            isLoading = true
        }
        _searchQuery.update { query }
    }

    fun search(query: String) {
        shouldShowError = false
        logRecipeResult = Utils.NONE
        viewModelScope.launch {
            foodRepository
                .searchRecipeForNutrition(query = query)
                .asFlowResult()
                .collect { result ->
                    when (result) {
                        is Result.Loading -> isLoading = true
                        is Result.Success -> {
                            isLoading = false
                            _recipes.update { result.data }
                        }

                        is Result.Error -> {
                            shouldShowError = true
                            isLoading = false
                        }
                    }
                }
        }
    }

    fun logRecipe(
        dateInMillis: Long,
        mealPosition: Int,
        recipe: NutritionSearchRecipe
    ) {
        shouldShowItemProcessLoading = true
        onProcessItemId = recipe.id

        viewModelScope.launch {
            when (userRepository.updateRecipeList(
                date = dateInMillis.toFirestoreDateTime(),
                mealPosition = mealPosition,
                recipe = recipe.toLogRecipe()
            )) {
                is Result.Loading -> Unit
                is Result.Success -> {
                    shouldShowItemProcessLoading = false
                    onProcessItemId = 0
                    logRecipeResult = Utils.SUCCESS
                }

                is Result.Error -> {
                    shouldShowItemProcessLoading = false
                    onProcessItemId = 0
                    logRecipeResult = Utils.FAIL
                }
            }
        }
    }

}
