package com.forknowledge.core.domain.di

import com.forknowledge.core.data.FoodRepository
import com.forknowledge.feature.model.MealSearchRecipe
import javax.inject.Inject

class AddToMealPlanInteractor @Inject constructor(
    private val foodRepository: FoodRepository
) {

    suspend operator fun invoke(
        dateInMillis: Long,
        mealPosition: Int,
        recipes: List<MealSearchRecipe>
    ) = foodRepository.addRecipeToMealPlan(
        username = "02efe52d-221f-4586-9f5b-265ffd008c77",
        hashKey = "0be5335eb7bc70b5665c5f451e2031fe692bea57",
        dateInMillis = dateInMillis,
        mealPosition = mealPosition,
        recipes = recipes
    )
}