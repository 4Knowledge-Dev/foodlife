package com.forknowledge.feature.recipe.screen.create

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.forknowledge.core.common.Result
import com.forknowledge.core.common.asFlowResult
import com.forknowledge.core.data.FoodRepository
import com.forknowledge.core.domain.di.CreateRecipeInteractor
import com.forknowledge.feature.model.Ingredient
import com.forknowledge.feature.model.Recipe
import com.forknowledge.feature.model.Step
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateRecipeViewModel @Inject constructor(
    private val foodRepository: FoodRepository,
    private val createRecipeInteractor: CreateRecipeInteractor
) : ViewModel() {

    private val _recipe = MutableStateFlow(Recipe())
    val recipe: StateFlow<Recipe> = _recipe.asStateFlow()

    var method by mutableStateOf("")
        private set

    var shouldShowLoading by mutableStateOf(false)
        private set

    var shouldShowSaveRecipeError by mutableStateOf(false)
        private set

    var onNavigateToRecipeDetail by mutableStateOf(false)
        private set

    fun saveRecipe() {
        shouldShowSaveRecipeError = false
        viewModelScope.launch {
            createRecipeInteractor(_recipe.value)
                .asFlowResult()
                .collect { result ->
                    when (result) {
                        is Result.Loading -> {
                            shouldShowLoading = true
                        }

                        is Result.Success -> {
                            shouldShowLoading = false
                            onNavigateToRecipeDetail = true
                        }

                        is Result.Error -> {
                            shouldShowLoading = false
                            shouldShowSaveRecipeError = true
                        }
                    }
                }
        }
    }

    fun updateRecipeName(recipeName: String) {
        _recipe.update { recipe ->
            recipe.copy(recipeName = recipeName)
        }
    }

    fun updateSummary(summary: String) {
        _recipe.update { recipe ->
            recipe.copy(summary = summary)
        }
    }

    fun updateServings(servings: Int) {
        _recipe.update { recipe ->
            recipe.copy(servings = servings)
        }
    }

    fun updatePrepTime(prepTime: Int) {
        _recipe.update { recipe ->
            recipe.copy(preparationMinutes = prepTime)
        }
    }

    fun updateCookTime(cookTime: Int) {
        _recipe.update { recipe ->
            recipe.copy(cookingMinutes = cookTime)
        }
    }

    fun updateRecipeSource(sourceName: String, sourceUrl: String) {
        _recipe.update { recipe ->
            recipe.copy(
                sourceName = sourceName,
                sourceUrl = sourceUrl
            )
        }
    }

    fun updateMethod(method: String) {
        this.method = method
        if (method.isNotEmpty()) {
            viewModelScope.launch {
                foodRepository.analyzeInstructions(instruction = method)
                    .asFlowResult()
                    .collect { result ->
                        when (result) {
                            is Result.Loading -> {}
                            is Result.Success -> {
                                val steps = result.data
                                val newIngredients = mutableListOf<Ingredient>()
                                steps.forEach { step ->
                                    newIngredients += step.ingredients
                                }
                                _recipe.update { recipe ->
                                    recipe.copy(
                                        ingredients = newIngredients,
                                        steps = steps
                                    )
                                }
                            }

                            is Result.Error -> {/* Do nothing */
                            }
                        }
                    }
            }
        }
    }

    fun addIngredient(ingredientName: String) {
        viewModelScope.launch {
            foodRepository.parseIngredient(ingredientName)
                .asFlowResult()
                .collect { result ->
                    when (result) {
                        is Result.Loading -> {}
                        is Result.Success -> {
                            _recipe.update { recipe ->
                                recipe.copy(
                                    ingredients = recipe.ingredients + result.data
                                )
                            }
                        }

                        is Result.Error -> {/* Do nothing */
                        }
                    }
                }
        }
    }

    fun removeIngredient(ingredient: Ingredient) {
        _recipe.update { recipe ->
            recipe.copy(
                ingredients = recipe.ingredients.filter { it != ingredient }
            )
        }
    }

    fun updateInstructions(step: Step) {
        _recipe.update { recipe ->
            recipe.copy(
                steps = if (recipe.steps.contains(step)) {
                    recipe.steps.filter { it != step }
                } else {
                    recipe.steps + step
                }
            )
        }
    }
}