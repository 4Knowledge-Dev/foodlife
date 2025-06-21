package com.forknowledge.core.domain.di

import com.forknowledge.core.data.UserRepository
import com.forknowledge.core.data.model.StatisticsDisplayData
import com.forknowledge.core.data.model.StatisticsNutrientRecord
import kotlinx.coroutines.flow.zip
import java.util.Date
import javax.inject.Inject

class GetNutritionStatisticsInteractor @Inject constructor(
    private val userRepository: UserRepository
) {

    operator fun invoke(
        startDate: Date,
        endDate: Date,
        nutritionName: String
    ) = userRepository.getUserInfo()
        .zip(userRepository.getNutritionRecordsInAMonth(startDate, endDate)) { user, records ->
            StatisticsDisplayData(
                targetCalories = user.targetNutrition.calories,
                targetCarbs = (user.targetNutrition.calories * user.targetNutrition.carbRatio).toFloat(),
                targetProteins = (user.targetNutrition.calories * user.targetNutrition.proteinRatio).toFloat(),
                targetFats = (user.targetNutrition.calories * user.targetNutrition.fatRatio).toFloat(),
                records = records.filter { record ->
                    record.nutrients.firstOrNull { it.name == nutritionName } != null
                }.map { record ->
                    StatisticsNutrientRecord(
                        date = record.date,
                        nutrient = record.nutrients.first { it.name == nutritionName }
                    )
                }
            )
        }
}
