package com.forknowledge.core.domain.di

import com.forknowledge.core.data.FoodRepository
import javax.inject.Inject

class DeleteFromMealPlanInteractor @Inject constructor(
    private val foodRepository: FoodRepository
) {

    suspend operator fun invoke(recipeId: Int) = foodRepository.deleteRecipeFromMealPlan(
        username = "02efe52d-221f-4586-9f5b-265ffd008c77",
        hashKey = "0be5335eb7bc70b5665c5f451e2031fe692bea57",
        recipeId = recipeId
    )
}
