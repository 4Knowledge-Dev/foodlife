package com.forknowledge.core.domain.di

import com.forknowledge.core.common.Result
import com.forknowledge.core.data.FoodRepository
import com.forknowledge.core.data.UserRepository
import com.forknowledge.feature.model.Recipe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class CreateRecipeInteractor @Inject constructor(
    private val foodRepository: FoodRepository,
    private val userRepository: UserRepository
) {

    operator fun invoke(recipe: Recipe): Flow<Result<Unit>> =
        foodRepository.analyzeRecipe(
            title = recipe.recipeName,
            servings = recipe.servings,
            ingredients = recipe.ingredients,
            steps = recipe.steps
        )
            .flatMapLatest { analyzedRecipe ->
                flow {
                    if (analyzedRecipe != null) {
                        val data = recipe.copy(
                            ingredients = analyzedRecipe.ingredients,
                            steps = analyzedRecipe.steps,
                            healthScore = analyzedRecipe.healthScore,
                            nutrition = analyzedRecipe.nutrition,
                            properties = analyzedRecipe.properties
                        )
                        when (val result = userRepository.saveRecipe(data)) {
                            is Result.Loading -> emit(Result.Loading)
                            is Result.Success -> {
                                emit(Result.Success(Unit))
                            }

                            is Result.Error -> {
                                emit(Result.Error(result.exception))
                            }
                        }
                    } else {
                        emit(Result.Error(IllegalStateException("Fail to analyze recipe!")))
                    }
                }
            }
}