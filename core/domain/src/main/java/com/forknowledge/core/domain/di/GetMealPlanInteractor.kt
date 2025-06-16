package com.forknowledge.core.domain.di

import com.forknowledge.core.data.FoodRepository
import javax.inject.Inject

class GetMealPlanInteractor @Inject constructor(
    private val foodRepository: FoodRepository,
    private val withUserToken: GetUserTokenInteractor
) {

    suspend operator fun invoke(startDate: String) = withUserToken { token ->
        foodRepository.getMealPlan(
            username = token.username,
            hashKey = token.hashKey,
            startDate = startDate
        )
    }
}
