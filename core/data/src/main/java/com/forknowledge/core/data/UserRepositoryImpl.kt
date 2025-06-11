package com.forknowledge.core.data

import android.util.Log
import com.forknowledge.core.common.Result
import com.forknowledge.core.common.extension.endOfDay
import com.forknowledge.core.common.extension.startOfDay
import com.forknowledge.core.data.model.NutritionDisplayData
import com.forknowledge.feature.model.IntakeNutrition
import com.forknowledge.feature.model.Recipe
import com.forknowledge.feature.model.User
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject
import kotlin.math.roundToLong

const val USER_COLLECTION = "User"
const val USER_RECORD_SUB_COLLECTION = "IntakeNutrition"
const val USER_RECORD_DATE_FIELD = "date"
const val USER_RECORD_RECIPE_LIST_FIELD = "recipes"
const val RECIPE_MEAL_TYPE_BREAKFAST = 0L
const val RECIPE_MEAL_TYPE_LUNCH = 1L
const val RECIPE_MEAL_TYPE_DINNER = 2L
const val RECIPE_MEAL_TYPE_SNACKS = 3L
const val RECIPE_NUTRIENT_CARB_INDEX = 3
const val RECIPE_NUTRIENT_PROTEIN_INDEX = 10
const val RECIPE_NUTRIENT_FAT_INDEX = 1

const val FIREBASE_EXCEPTION = "FirebaseException"
const val FIREBASE_GET_DATA_EXCEPTION = "FirebaseDataException"

class UserRepositoryImpl @Inject constructor(
    firestore: FirebaseFirestore
) : UserRepository {

    val userRef = firestore.collection(USER_COLLECTION).document("1")

    override fun getUserTargetNutrition() = callbackFlow {
        val listenerRegistration = userRef
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e(FIREBASE_EXCEPTION, "Get data failed with ", error)
                    channel.trySend(null)
                    return@addSnapshotListener
                } else {
                    if (snapshot != null && snapshot.exists()) {
                        val user = snapshot.toObject(User::class.java) ?: User()
                        channel.trySend(user.targetNutrition)
                    } else {
                        Log.d(FIREBASE_GET_DATA_EXCEPTION, "No data found.")
                        channel.trySend(null)
                    }
                }
            }

        awaitClose {
            listenerRegistration.remove()
        }
    }
        .flowOn(Dispatchers.IO)
        .conflate()

    override fun getUserNutritionRecordByDate(date: Date) = callbackFlow {
        val listenerRegistration = userRef.collection(USER_RECORD_SUB_COLLECTION)
            .whereGreaterThanOrEqualTo(USER_RECORD_DATE_FIELD, date.startOfDay())
            .whereLessThan(USER_RECORD_DATE_FIELD, date.endOfDay())
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e(FIREBASE_EXCEPTION, "Get data failed with ", error)
                    channel.trySend(NutritionDisplayData())
                    return@addSnapshotListener
                }
                if (snapshot != null && !snapshot.isEmpty) {
                    try {
                        snapshot.documents[0].toObject(IntakeNutrition::class.java)
                            ?.let { nutrition ->
                                val recipes = nutrition.recipes
                                val mealCalories = listOf(
                                    recipes.filter { it.meal == RECIPE_MEAL_TYPE_BREAKFAST }
                                        .sumOf { it.nutrients.first().amount },
                                    recipes.filter { it.meal == RECIPE_MEAL_TYPE_LUNCH }
                                        .sumOf { it.nutrients.first().amount },
                                    recipes.filter { it.meal == RECIPE_MEAL_TYPE_DINNER }
                                        .sumOf { it.nutrients.first().amount },
                                    recipes.filter { it.meal == RECIPE_MEAL_TYPE_SNACKS }
                                        .sumOf { it.nutrients.first().amount }
                                )
                                channel.trySend(
                                    NutritionDisplayData(
                                        calories = mealCalories.sum().roundToLong(),
                                        carbs = recipes.sumOf { it.nutrients[RECIPE_NUTRIENT_CARB_INDEX].amount.roundToLong() },
                                        proteins = recipes.sumOf { it.nutrients[RECIPE_NUTRIENT_PROTEIN_INDEX].amount.roundToLong() },
                                        fats = recipes.sumOf { it.nutrients[RECIPE_NUTRIENT_FAT_INDEX].amount.roundToLong() },
                                        mealCalories = mealCalories.map { it.roundToLong() }
                                    )
                                )
                            } ?: channel.trySend(NutritionDisplayData())

                    } catch (e: Exception) {
                        Log.e(FIREBASE_EXCEPTION, "Get data failed with ", e)
                        channel.trySend(NutritionDisplayData())
                    }
                } else {
                    Log.d(FIREBASE_GET_DATA_EXCEPTION, "No record found at date $date (real-time).")
                    channel.trySend(NutritionDisplayData())
                }
            }

        awaitClose {
            listenerRegistration.remove()
        }
    }
        .flowOn(Dispatchers.IO)
        .conflate()

    override suspend fun createNewTrackDay(
        documentId: String,
        date: Date,
        recipe: Recipe
    ) = withContext(Dispatchers.IO) {
        return@withContext try {
            userRef.collection(USER_RECORD_SUB_COLLECTION).document(documentId.toString())
                .set(IntakeNutrition(date, listOf(recipe)))
                .await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun updateRecipeList(
        documentId: String,
        recipe: Recipe,
        isAdd: Boolean
    ) = withContext(Dispatchers.IO) {
        return@withContext try {
            userRef.collection(USER_RECORD_SUB_COLLECTION).document(documentId.toString())
                .update(USER_RECORD_RECIPE_LIST_FIELD, FieldValue.arrayUnion(recipe))
                .await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
