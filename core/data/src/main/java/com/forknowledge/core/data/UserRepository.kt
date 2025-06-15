package com.forknowledge.core.data

import com.forknowledge.core.common.Result
import com.forknowledge.core.data.model.NutritionDisplayData
import com.forknowledge.core.data.datatype.UserAuthState
import com.forknowledge.feature.model.SearchRecipe
import com.forknowledge.feature.model.userdata.TargetNutrition
import com.forknowledge.feature.model.userdata.User
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface UserRepository {

    /**
     * Get user's flow from app startup.
     * @return [UserAuthState] of user in time app startup.
     */
    fun getUserFlow(): Flow<UserAuthState>

    /**
     * Get user's hash key.
     * @return user's hash key.
     */
    fun getHashKey(): Flow<String>

    /**
     * Update user's information.
     * @param [user] the user to update.
     * @return [Result] of operation.
     */
    suspend fun updateUserInfo(user: User): Result<Unit>

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
        recipe: SearchRecipe
    ): Result<Unit>

    /**
     * Update the logged recipe list of a day.
     * @param [documentId] the id of document updated.
     * @param [recipe] the recipe to add or remove.
     * @return [Result] of operation.
     */
    suspend fun updateRecipeList(
        documentId: String,
        recipe: SearchRecipe,
        isAdd: Boolean
    ): Result<Unit>
}
