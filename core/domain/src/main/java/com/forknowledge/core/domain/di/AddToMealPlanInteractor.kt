package com.forknowledge.core.domain.di

import com.forknowledge.core.data.FoodRepository
import com.forknowledge.feature.model.MealSearchRecipe
import javax.inject.Inject

class AddToMealPlanInteractor @Inject constructor(
    private val foodRepository: FoodRepository,
    private val withUserToken: GetUserTokenInteractor
) {

    suspend operator fun invoke(
        dateInMillis: Long,
        mealPosition: Int,
        recipes: List<MealSearchRecipe>
    ) = withUserToken { token ->
        foodRepository.addRecipeToMealPlan(
            username = token.username,
            hashKey = token.hashKey,
            dateInMillis = dateInMillis,
            mealSlot = mealPosition,
            recipes = recipes
        )
    }
}
