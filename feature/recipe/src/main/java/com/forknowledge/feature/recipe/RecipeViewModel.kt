package com.forknowledge.feature.recipe

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.forknowledge.core.common.Result
import com.forknowledge.core.common.ResultState
import com.forknowledge.core.common.asFlowResult
import com.forknowledge.core.common.extension.toFirestoreDateTime
import com.forknowledge.core.data.FoodRepository
import com.forknowledge.core.data.UserRepository
import com.forknowledge.feature.model.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val foodRepository: FoodRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _recipe = MutableStateFlow<Recipe?>(null)
    val recipe: StateFlow<Recipe?> = _recipe

    var shouldShowLoading by mutableStateOf(false)
        private set

    var shouldShowCompleteLoading by mutableStateOf(false)
        private set

    var logRecipeResult by mutableStateOf<ResultState?>(null)
        private set

    fun getRecipe(id: Int) {
        viewModelScope.launch {
            foodRepository.getRecipeInformation(id)
                .asFlowResult()
                .collect { result ->
                    when (result) {
                        is Result.Loading -> {
                            shouldShowLoading = true
                        }

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

    fun completeRecipe(
        dateInMillis: Long,
        mealPosition: Int,
        servings: Int
    ) {
        shouldShowCompleteLoading = true
        viewModelScope.launch {
            when (userRepository.updateRecipeList(
                date = dateInMillis.toFirestoreDateTime(),
                mealPosition = mealPosition,
                recipe = _recipe.value!!.copy(servings = servings).toLogRecipe()
            )) {
                is Result.Loading -> Unit
                is Result.Success -> {
                    shouldShowCompleteLoading = false
                    logRecipeResult = ResultState.SUCCESS
                }

                is Result.Error -> {
                    shouldShowCompleteLoading = false
                    logRecipeResult = ResultState.FAILURE
                }
            }
        }
    }
}
