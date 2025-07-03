package com.forknowledge.core.domain.di

import com.forknowledge.core.common.Result
import com.forknowledge.core.common.healthtype.toDiet
import com.forknowledge.core.data.FoodRepository
import com.forknowledge.core.data.UserRepository
import com.forknowledge.core.data.model.MealPlanDisplayData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GenerateMealPlanInteractor @Inject constructor(
    private val addToMealPlanInteractor: AddToMealPlanInteractor,
    private val getMealPlanInteractor: GetMealPlanInteractor,
    private val clearMealPlanInteractor: ClearMealPlanInteractor,
    private val userRepository: UserRepository,
    private val foodRepository: FoodRepository
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend operator fun invoke(mealPlan: List<MealPlanDisplayData>): Result<List<MealPlanDisplayData>> =
        try{
            when (val result = clearMealPlanInteractor(mealPlan)) {
                is Result.Loading -> Result.Loading
                is Result.Success -> {
                    userRepository.getUserInfo()
                        .flatMapConcat { user ->
                            foodRepository.generateMealPlan(
                                targetCalories = user.targetNutrition.calories.toInt(),
                                diet = user.diet.toDiet().typeUrl,
                                excludeIngredients = user.excludes.joinToString(", ")
                            )
                        }
                        .map { recipes ->
                            addToMealPlanInteractor(recipes = recipes)
                        }
                        .map { result ->
                            when (result) {
                                is Result.Loading -> Result.Loading
                                is Result.Success -> getMealPlanInteractor()
                                is Result.Error -> Result.Error(result.exception)
                            }
                        }
                        .first()
                }

                is Result.Error -> Result.Error(result.exception)
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
}