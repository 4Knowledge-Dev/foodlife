package com.forknowledge.core.data

import com.forknowledge.core.data.model.NutritionDisplayData
import com.forknowledge.feature.model.TargetNutrition
import com.forknowledge.feature.model.IntakeNutrition
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface UserRepository {

    /**
     * Get user's target nutrition.
     * @return 4 target nutrition values: calories, carbs, proteins, fats.
     */
    fun getUserTargetNutrition(): Flow<TargetNutrition?>

    /**
     * Get user's nutrition record by specific date.
     * @param [date] the date to get nutrition record.
     * @return [NutritionDisplayData] to display today tracking.
     */
    fun getUserNutritionRecordByDate(date: Date): Flow<NutritionDisplayData>
}
