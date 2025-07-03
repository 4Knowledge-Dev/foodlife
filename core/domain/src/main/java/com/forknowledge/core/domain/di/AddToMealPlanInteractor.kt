package com.forknowledge.core.domain.di

import com.forknowledge.core.data.FoodRepository
import com.forknowledge.feature.model.AddToMealPlanRecipe
import javax.inject.Inject

class AddToMealPlanInteractor @Inject constructor(
    private val foodRepository: FoodRepository,
    private val withUserToken: GetUserTokenInteractor
) {

    suspend operator fun invoke(recipes: List<AddToMealPlanRecipe>) = withUserToken { token ->
        foodRepository.addRecipeToMealPlan(
            username = token.username,
            hashKey = token.hashKey,
            recipes = recipes
        )
    }
}
