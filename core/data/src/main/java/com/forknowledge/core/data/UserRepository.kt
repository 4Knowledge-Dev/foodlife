package com.forknowledge.core.data

import com.forknowledge.feature.model.Nutrition
import com.forknowledge.feature.model.Record
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface UserRepository {

    /**
     * Get user's target nutrition.
     * @return 4 target nutrition values: calories, carbs, proteins, fats.
     */
    fun getUserTargetNutrition(): Flow<Nutrition?>

    /**
     * Get user's nutrition record by specific date.
     * @param [date] the date to get nutrition record.
     * @return a nutrition record.
     */
    fun getUserNutritionRecordByDate(date: Date): Flow<Record>
}
