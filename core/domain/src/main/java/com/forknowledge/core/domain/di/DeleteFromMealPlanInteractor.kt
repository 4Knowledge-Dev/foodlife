package com.forknowledge.core.domain.di

import com.forknowledge.core.data.FoodRepository
import javax.inject.Inject

class DeleteFromMealPlanInteractor @Inject constructor(
    private val foodRepository: FoodRepository,
    private val withUserToken: GetUserTokenInteractor
) {

    suspend operator fun invoke(recipeId: Int) = withUserToken { token ->
        foodRepository.deleteRecipeFromMealPlan(
            username = token.username,
            hashKey = token.hashKey,
            recipeId = recipeId
        )
    }
}
