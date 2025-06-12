package com.forknowledge.core.data

import com.forknowledge.core.common.Result
import com.forknowledge.core.data.model.NutritionDisplayData
import com.forknowledge.core.data.model.UserAuthState
import com.forknowledge.feature.model.Recipe
import com.forknowledge.feature.model.TargetNutrition
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface UserRepository {


    /**
     * Get user's information.
     * @return [UserAuthState] of user.
     */
    fun getUserFlow(): Flow<UserAuthState>

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

    /**
     * Log recipe to a day and create a new firestore document.
     * @param [documentId] the id of document to create.
     * @param [date] the date to log recipe.
     * @param [recipe] the recipe to add.
     * @return [Result] of operation.
     */
    suspend fun createNewTrackDay(
        documentId: String,
        date: Date,
        recipe: Recipe
    ): Result<Unit>

    /**
     * Update the logged recipe list of a day.
     * @param [documentId] the id of document updated.
     * @param [recipe] the recipe to add or remove.
     * @return [Result] of operation.
     */
    suspend fun updateRecipeList(
        documentId: String,
        recipe: Recipe,
        isAdd: Boolean
    ): Result<Unit>
}
