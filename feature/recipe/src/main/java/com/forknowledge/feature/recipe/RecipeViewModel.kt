package com.forknowledge.feature.recipe

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.forknowledge.core.common.Result
import com.forknowledge.core.common.asFlowResult
import com.forknowledge.core.data.FoodRepository
import com.forknowledge.feature.model.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val foodRepository: FoodRepository
): ViewModel() {

    private val _recipe = MutableStateFlow<Recipe?>(null)
    val recipe: StateFlow<Recipe?> = _recipe

    var shouldShowLoading by mutableStateOf(false)
        private set

    fun getRecipe(id: Int) {
        viewModelScope.launch {
            foodRepository.getRecipeInformation(id)
                .asFlowResult()
                .collect { result ->
                    when(result) {
                        is Result.Loading -> { shouldShowLoading = true }
                        is Result.Success -> {
                            shouldShowLoading = false
                            _recipe.update { result.data }
                        }
                        is Result.Error -> {
                            shouldShowLoading = false
                        }
                    }
                }
        }
    }
}
