package com.forknowledge.core.data

import android.util.Log
import androidx.datastore.core.IOException
import com.forknowledge.core.common.Result
import com.forknowledge.core.common.extension.endOfDay
import com.forknowledge.core.common.extension.startOfDay
import com.forknowledge.core.data.datasource.PreferenceDatastore
import com.forknowledge.core.data.datatype.UserAuthState
import com.forknowledge.core.data.model.NutritionDisplayData
import com.forknowledge.core.data.reference.FirebaseException.FIREBASE_EXCEPTION
import com.forknowledge.core.data.reference.FirebaseException.FIREBASE_GET_DATA_EXCEPTION
import com.forknowledge.core.data.reference.FirestoreReference.USER_COLLECTION
import com.forknowledge.core.data.reference.FirestoreReference.USER_RECORD_SUB_COLLECTION
import com.forknowledge.feature.model.SearchRecipe
import com.forknowledge.feature.model.userdata.IntakeNutrition
import com.forknowledge.feature.model.userdata.User
import com.forknowledge.feature.model.userdata.UserToken
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject
import kotlin.math.roundToLong

const val USER_RECORD_DATE_FIELD = "date"
const val USER_RECORD_RECIPE_LIST_FIELD = "recipes"
const val RECIPE_MEAL_TYPE_BREAKFAST = 0L
const val RECIPE_MEAL_TYPE_LUNCH = 1L
const val RECIPE_MEAL_TYPE_DINNER = 2L
const val RECIPE_MEAL_TYPE_SNACKS = 3L
const val RECIPE_NUTRIENT_CARB_INDEX = 3
const val RECIPE_NUTRIENT_PROTEIN_INDEX = 10
const val RECIPE_NUTRIENT_FAT_INDEX = 1

class UserRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val datastore: PreferenceDatastore,
    private val firestore: FirebaseFirestore
) : UserRepository {

    override suspend fun getUserTokenFromLocal(): Result<UserToken> {
        return try {
            Result.Success(datastore.getUserToken())
        } catch (e: IOException) {
            Result.Error(e)
        }
    }

    override suspend fun saveUserTokenToLocal(username: String, hashKey: String) =
        datastore.saveUserToken(
            username = username,
            hashKey = hashKey
        )

    override suspend fun getUserTokenFromServer(): Result<UserToken> = withContext(Dispatchers.IO) {
        return@withContext try {
            val user = firestore.collection(USER_COLLECTION).document(auth.currentUser!!.uid)
                .get()
                .await()
                .toObject(User::class.java)
            Result.Success(
                UserToken(
                    username = user?.username ?: "",
                    hashKey = user?.hashKey ?: ""
                )
            )
        } catch (e: FirebaseException) {
            Log.e(FIREBASE_GET_DATA_EXCEPTION, "Get data failed with ", e)
            Result.Error(Exception(e))
        }
    }

    override suspend fun saveUserTokenToServer(
        username: String,
        hashKey: String
    ) = withContext(Dispatchers.IO) {
        return@withContext try {
            firestore.collection(USER_COLLECTION).document(auth.currentUser!!.uid)
                .set(User(username = username, hashKey = hashKey), SetOptions.merge())
                .await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override fun getUserStateFlow() = flow {
        if (auth.currentUser == null) {
            emit(UserAuthState.UNAUTHENTICATED)
        } else {
            try {
                val userDocument =
                    firestore.collection(USER_COLLECTION).document(auth.currentUser!!.uid)
                        .get()
                        .await()

                if (userDocument.exists()) {
                    val user = userDocument.toObject(User::class.java)
                    user?.let {
                        if (user.isNewUser) {
                            emit(UserAuthState.NEW_USER)
                        } else {
                            emit(UserAuthState.OLD_USER)
                        }
                    } ?: emit(UserAuthState.NONE)
                } else {
                    Log.d(FIREBASE_GET_DATA_EXCEPTION, "No data found.")
                    emit(UserAuthState.NONE)
                }
            } catch (e: Exception) {
                Log.e(FIREBASE_EXCEPTION, "Get user data failed with ", e)
                emit(UserAuthState.NONE)
            }
        }
    }

    override suspend fun updateUserInfo(user: User): Result<Unit> {
        return try {
            firestore.collection(USER_COLLECTION).document(auth.currentUser!!.uid)
                .set(user, SetOptions.merge())
                .await()
            Result.Success(Unit)
        } catch (e: FirebaseException) {
            Log.e(FIREBASE_EXCEPTION, "Update data failed with ", e)
            Result.Error(e)
        } catch (e: Exception) {
            Log.e(FIREBASE_EXCEPTION, "Update data failed with ", e)
            Result.Error(e)
        }
    }

    override fun getUserTargetNutrition() = callbackFlow {
        val listenerRegistration =
            firestore.collection(USER_COLLECTION).document(auth.currentUser!!.uid)
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
        val listenerRegistration =
            firestore.collection(USER_COLLECTION).document(auth.currentUser!!.uid)
                .collection(USER_RECORD_SUB_COLLECTION)
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
                        Log.d(
                            FIREBASE_GET_DATA_EXCEPTION,
                            "No record found at date $date (real-time)."
                        )
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
        recipe: SearchRecipe
    ) = withContext(Dispatchers.IO) {
        return@withContext try {
            firestore.collection(USER_COLLECTION).document(auth.currentUser!!.uid)
                .collection(USER_RECORD_SUB_COLLECTION).document(documentId.toString())
                .set(IntakeNutrition(date, listOf(recipe)))
                .await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun updateRecipeList(
        documentId: String,
        recipe: SearchRecipe,
        isAdd: Boolean
    ) = withContext(Dispatchers.IO) {
        return@withContext try {
            firestore.collection(USER_COLLECTION).document(auth.currentUser!!.uid)
                .collection(USER_RECORD_SUB_COLLECTION).document(documentId.toString())
                .update(USER_RECORD_RECIPE_LIST_FIELD, FieldValue.arrayUnion(recipe))
                .await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
