package com.forknowledge.core.domain.di

import com.forknowledge.core.data.FoodRepository
import com.forknowledge.core.data.UserRepository
import javax.inject.Inject

class GetMealPlanInteractor @Inject constructor(
    private val foodRepository: FoodRepository,
    private val userRepository: UserRepository
) {

    operator fun invoke() = foodRepository.getMealPlan(
        username = "02efe52d-221f-4586-9f5b-265ffd008c77",
        hashKey = "0be5335eb7bc70b5665c5f451e2031fe692bea57",
        startDate = "2025-06-08"
    )
}
