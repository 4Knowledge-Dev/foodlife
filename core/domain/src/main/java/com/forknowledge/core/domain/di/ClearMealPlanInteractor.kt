package com.forknowledge.core.domain.di

import com.forknowledge.core.common.Result
import com.forknowledge.core.data.FoodRepository
import com.forknowledge.core.data.model.MealPlanDisplayData
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

class ClearMealPlanInteractor @Inject constructor(
    private val withUserToken: GetUserTokenInteractor,
    private val foodRepository: FoodRepository
) {

    suspend operator fun invoke(meals: List<MealPlanDisplayData>): Result<Unit> =
        withContext(Dispatchers.IO) {
            return@withContext try {
                val deferredResults = meals.map { meal ->
                    async {
                        withUserToken { token ->
                            foodRepository.clearMealPlanDay(
                                date = meal.date.toString(),
                                username = token.username,
                                hashKey = token.hashKey
                            )
                        }
                    }
                }
                val result = deferredResults.awaitAll()
                if (result.firstOrNull { it is Result.Error } == null) {
                    Result.Success(Unit)
                } else {
                    Result.Error(Exception("Failed to clear meal plan."))
                }
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
}