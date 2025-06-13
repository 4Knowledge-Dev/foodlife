package com.forknowledge.feature.nutrient.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.forknowledge.core.common.Result
import com.forknowledge.core.common.asFlowResult
import com.forknowledge.core.common.extension.toFirestoreDocumentIdByDate
import com.forknowledge.core.data.FoodRepository
import com.forknowledge.core.data.UserRepository
import com.forknowledge.feature.model.userdata.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
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

const val SEARCH_DEBOUNCE = 500L

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val foodRepository: FoodRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow<String>("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _recipes = MutableStateFlow<PagingData<Recipe>>(PagingData.empty())
    val recipes: StateFlow<PagingData<Recipe>> = _recipes

    var isLoading by mutableStateOf(false)
        private set

    var logRecipeResult by mutableStateOf<Result<Unit>?>(null)
        private set

    var loggedRecipeId by mutableStateOf<Long?>(null)
        private set

    private var hasLoggedFood = false

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

    fun updateHasLoggedFood(isLogged: Boolean) {
        hasLoggedFood = isLogged
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

    fun logRecipe(
        meal: Long,
        date: Date,
        recipe: Recipe
    ) {
        logRecipeResult = Result.Loading
        loggedRecipeId = recipe.id
        viewModelScope.launch {
            if (hasLoggedFood) {
                when (val result = userRepository.updateRecipeList(
                    documentId = date.toFirestoreDocumentIdByDate(),
                    recipe = recipe.copy(meal = meal),
                    isAdd = true
                )) {
                    is Result.Loading -> Unit
                    is Result.Success -> {
                        logRecipeResult = Result.Success(Unit)
                        delay(1000L)
                        loggedRecipeId = null
                    }

                    is Result.Error -> {
                        logRecipeResult = Result.Error(result.exception)
                        delay(1000L)
                        loggedRecipeId = null
                    }
                }
            } else {
                when (val result = userRepository.createNewTrackDay(
                    documentId = date.toFirestoreDocumentIdByDate(),
                    date = date,
                    recipe = recipe.copy(meal = meal)
                )) {
                    is Result.Loading -> Unit
                    is Result.Success -> {
                        hasLoggedFood = true
                        logRecipeResult = Result.Success(Unit)
                        loggedRecipeId = null
                    }

                    is Result.Error -> {
                        logRecipeResult = Result.Error(result.exception)
                        loggedRecipeId = null
                    }
                }
            }
        }
    }

}
