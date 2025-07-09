package com.forknowledge.feature.explore.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.forknowledge.core.common.AppConstant.SEARCH_DEBOUNCE
import com.forknowledge.core.common.Result
import com.forknowledge.core.common.asFlowResult
import com.forknowledge.core.data.FoodRepository
import com.forknowledge.core.domain.di.AddToMealPlanInteractor
import com.forknowledge.feature.model.AddToMealPlanRecipe
import com.forknowledge.feature.model.MealSearchRecipe
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
import javax.inject.Inject

const val SNACK_BAR_ADD_RECIPE_ERROR_DELAY = 2000L
const val NUTRIENTS_CALORIES_INDEX = 0
const val NUTRIENTS_CARB_INDEX = 1
const val NUTRIENTS_PROTEIN_INDEX = 2
const val NUTRIENTS_FAT_INDEX = 3

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val addToMealPlanInteractor: AddToMealPlanInteractor,
    private val foodRepository: FoodRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _recipes = MutableStateFlow<PagingData<MealSearchRecipe>>(PagingData.empty())
    val recipes: StateFlow<PagingData<MealSearchRecipe>> = _recipes

    private val _selectedRecipes = MutableStateFlow(emptyList<MealSearchRecipe>())
    val selectedRecipes: StateFlow<List<MealSearchRecipe>> = _selectedRecipes

    var isLoading by mutableStateOf(false)
        private set

    var shouldShowLoadingButton by mutableStateOf(false)
        private set

    var shouldShowError by mutableStateOf(false)
        private set

    var onNavigateToMealPlan by mutableStateOf(false)
        private set

    init {
        viewModelScope.launch { observeQueryChanges() }
    }

    private fun observeQueryChanges() {
        _searchQuery
            .debounce(SEARCH_DEBOUNCE)
            .filter { it.isNotBlank() }
            .flatMapLatest { query ->
                foodRepository.searchRecipeForMeal(query = query)
            }
            .cachedIn(viewModelScope)
            .asFlowResult()
            .onEach { result ->
                if (_searchQuery.value.isNotEmpty()) {
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
            .launchIn(viewModelScope)
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.update { query }
    }

    fun updateSelectedRecipes(recipe: MealSearchRecipe) {
        _selectedRecipes.update {
            if (it.contains(recipe)) {
                it - recipe
            } else {
                it + recipe
            }
        }
    }

    fun clearSelectedRecipes() {
        _selectedRecipes.update { emptyList() }
    }

    fun search(query: String) {
        viewModelScope.launch {
            foodRepository.searchRecipeForMeal(query = query)
                .cachedIn(viewModelScope)
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

    fun addToMealPlan(
        dateInMillis: Long,
        meal: Int,
    ) {
        shouldShowLoadingButton = true
        val addRecipes = _selectedRecipes.value.map { recipe ->
            AddToMealPlanRecipe(
                dateInMillis = dateInMillis,
                meal = meal,
                recipeId = recipe.id,
                recipeName = recipe.name,
                imageUrl = recipe.imageUrl,
                servings = recipe.servings,
                cookTime = recipe.cookTime,
                calories = recipe.nutrients[NUTRIENTS_CALORIES_INDEX],
                carbs = recipe.nutrients[NUTRIENTS_CARB_INDEX],
                protein = recipe.nutrients[NUTRIENTS_PROTEIN_INDEX],
                fat = recipe.nutrients[NUTRIENTS_FAT_INDEX]
            )
        }

        viewModelScope.launch {
            when (addToMealPlanInteractor(recipes = addRecipes)) {
                is Result.Loading -> shouldShowLoadingButton = true
                is Result.Success -> {
                    clearSelectedRecipes()
                    shouldShowLoadingButton = false
                    onNavigateToMealPlan = true
                }

                is Result.Error -> {
                    clearSelectedRecipes()
                    shouldShowLoadingButton = false
                    shouldShowError = true
                    delay(SNACK_BAR_ADD_RECIPE_ERROR_DELAY)
                    shouldShowError = false
                }
            }
        }
    }
}
