package com.forknowledge.feature.recipe.screen.savedrecipe

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.forknowledge.core.common.Result
import com.forknowledge.core.common.asFlowResult
import com.forknowledge.core.data.UserRepository
import com.forknowledge.feature.model.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedRecipeViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes: StateFlow<List<Recipe>> = _recipes.asStateFlow()

    var shouldShowLoading by mutableStateOf(false)
        private set

    var shouldShowError by mutableStateOf(false)
        private set

    init {
        getSavedRecipes()
    }

    fun getSavedRecipes() {
        viewModelScope.launch {
            userRepository.getSavedRecipeList()
                .asFlowResult()
                .collect { result ->
                    when(result) {
                        is Result.Loading -> { shouldShowLoading = true }
                        is Result.Success -> {
                            shouldShowLoading = false
                            _recipes.update { result.data }
                        }
                        is Result.Error -> {
                            shouldShowLoading = false
                            shouldShowError = true
                        }
                    }
                }
        }
    }
}