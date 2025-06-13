package com.forknowledge.core.domain.di

import com.forknowledge.core.data.FoodRepository
import com.forknowledge.core.data.UserRepository
import javax.inject.Inject

class GetMealPlanInteractor @Inject constructor(
    private val foodRepository: FoodRepository,
    private val userRepository: UserRepository
) {

    suspend operator fun invoke() {
        userRepository.getHashKey().collect { hashKey ->
            if ()
    }
}
