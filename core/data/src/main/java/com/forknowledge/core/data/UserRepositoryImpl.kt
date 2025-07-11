package com.forknowledge.core.data

import android.util.Log
import androidx.datastore.core.IOException
import com.forknowledge.core.common.AppConstant.RECIPE_NUTRIENT_CALORIES_INDEX
import com.forknowledge.core.common.Result
import com.forknowledge.core.common.extension.endOfDay
import com.forknowledge.core.common.extension.startOfDay
import com.forknowledge.core.common.extension.toFirestoreDocumentIdByDate
import com.forknowledge.core.common.healthtype.NutrientType
import com.forknowledge.core.common.nutrientRatioToNutrientAmount
import com.forknowledge.core.data.datasource.PreferenceDatastore
import com.forknowledge.core.data.datatype.UserAuthState
import com.forknowledge.core.data.model.DailyNutritionDisplayData
import com.forknowledge.core.data.model.NutritionDisplayData
import com.forknowledge.core.data.model.TargetNutritionDisplayData
import com.forknowledge.core.data.reference.FirebaseException.FIREBASE_EXCEPTION
import com.forknowledge.core.data.reference.FirebaseException.FIREBASE_GET_DATA_EXCEPTION
import com.forknowledge.core.data.reference.FirebaseException.FIREBASE_TRANSACTION_EXCEPTION
import com.forknowledge.core.data.reference.FirestoreReference
import com.forknowledge.core.data.reference.FirestoreReference.USER_COLLECTION
import com.forknowledge.core.data.reference.FirestoreReference.USER_RECIPE_ID_FIELD
import com.forknowledge.core.data.reference.FirestoreReference.USER_RECIPE_SUB_COLLECTION
import com.forknowledge.core.data.reference.FirestoreReference.USER_RECORD_SUB_COLLECTION
import com.forknowledge.feature.model.Recipe
import com.forknowledge.feature.model.userdata.IntakeNutrition
import com.forknowledge.feature.model.userdata.LogRecipe
import com.forknowledge.feature.model.userdata.NutrientData
import com.forknowledge.feature.model.userdata.NutritionRecipeData
import com.forknowledge.feature.model.userdata.User
import com.forknowledge.feature.model.userdata.UserToken
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject
import kotlin.math.roundToInt

const val USER_RECORD_DATE_FIELD = "date"
const val RECIPE_MEAL_TYPE_BREAKFAST = 1L
const val RECIPE_MEAL_TYPE_LUNCH = 2L
const val RECIPE_MEAL_TYPE_DINNER = 3L
const val RECIPE_MEAL_TYPE_SNACKS = 4L

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
        val updateUser = mapOf(
            FirestoreReference.USER_DOCUMENT_USERNAME_FIELD to username,
            FirestoreReference.USER_DOCUMENT_HASH_KEY_FIELD to hashKey
        )
        return@withContext try {
            firestore.collection(USER_COLLECTION).document(auth.currentUser!!.uid)
                .update(updateUser)
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
                            val targetCalories = user.targetNutrition.calories
                            channel.trySend(
                                TargetNutritionDisplayData(
                                    calories = targetCalories.toInt(),
                                    carbs = nutrientRatioToNutrientAmount(
                                        nutrient = NutrientType.CARBOHYDRATE,
                                        totalCalories = targetCalories.toInt(),
                                        ratio = user.targetNutrition.carbRatio
                                    ),
                                    protein = nutrientRatioToNutrientAmount(
                                        nutrient = NutrientType.PROTEIN,
                                        totalCalories = targetCalories.toInt(),
                                        ratio = user.targetNutrition.proteinRatio
                                    ),
                                    fat = nutrientRatioToNutrientAmount(
                                        nutrient = NutrientType.FAT,
                                        totalCalories = targetCalories.toInt(),
                                        ratio = user.targetNutrition.fatRatio
                                    ),
                                    breakfastCalories = (targetCalories * user.targetNutrition.breakfastRatio).roundToInt(),
                                    lunchCalories = (targetCalories * user.targetNutrition.lunchRatio).roundToInt(),
                                    dinnerCalories = (targetCalories * user.targetNutrition.dinnerRatio).roundToInt(),
                                    snacksCalories = (targetCalories * user.targetNutrition.snacksRatio).roundToInt(),
                                )
                            )
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
                            snapshot.documents[0].toObject(IntakeNutrition::class.java) // Query get only one day.
                                ?.let { nutrition ->
                                    val recipes = nutrition.recipes
                                    val mealCalories = listOf(
                                        recipes
                                            .filter { it.mealPosition == RECIPE_MEAL_TYPE_BREAKFAST }
                                            .sumOf { it.calories.toDouble() },
                                        recipes
                                            .filter { it.mealPosition == RECIPE_MEAL_TYPE_LUNCH }
                                            .sumOf { it.calories.toDouble() },
                                        recipes
                                            .filter { it.mealPosition == RECIPE_MEAL_TYPE_DINNER }
                                            .sumOf { it.calories.toDouble() },
                                        recipes
                                            .filter { it.mealPosition == RECIPE_MEAL_TYPE_SNACKS }
                                            .sumOf { it.calories.toDouble() }
                                    )
                                    channel.trySend(
                                        NutritionDisplayData(
                                            nutrients = nutrition.nutrients.ifEmpty {
                                                null
                                            },
                                            mealCalories = mealCalories.map { it.toInt() }
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

    override suspend fun updateRecipeList(
        date: Date,
        mealPosition: Int,
        recipe: LogRecipe
    ) = withContext(Dispatchers.IO) {
        val documentId = date.toFirestoreDocumentIdByDate()
        val collectionRef = firestore.collection(USER_COLLECTION).document(auth.currentUser!!.uid)
            .collection(USER_RECORD_SUB_COLLECTION)
        var newIntakeNutrition = IntakeNutrition()

        val transaction = firestore.runTransaction { transaction ->
            val snapshot = transaction.get(collectionRef.document(documentId))

            if (snapshot.exists()) {
                val intakeNutrition =
                    snapshot.toObject(IntakeNutrition::class.java) ?: IntakeNutrition()
                val oldNutrientRecords = intakeNutrition.nutrients
                val oldRecipeRecords = intakeNutrition.recipes
                val newNutrientRecords = mutableListOf<NutrientData>()
                var newRecipeRecords = emptyList<NutritionRecipeData>()

                // Update recipe list, update servings and calories of recipe if it existed, otherwise add new recipe.
                if (oldRecipeRecords.any { it.mealPosition == mealPosition.toLong() && it.id == recipe.id.toLong() }) {
                    newRecipeRecords = oldRecipeRecords.map { recipeRecord ->
                        if (recipeRecord.id == recipe.id.toLong() && recipeRecord.mealPosition == mealPosition.toLong()) {
                            recipeRecord.copy(
                                servings = recipeRecord.servings + recipe.servings.toLong(),
                                calories = recipeRecord.calories + recipe.nutrients[RECIPE_NUTRIENT_CALORIES_INDEX].amount.toLong() * recipe.servings,

                                )
                        } else {
                            recipeRecord
                        }
                    }
                } else {
                    val newRecipeRecord = NutritionRecipeData(
                        id = recipe.id.toLong(),
                        name = recipe.name,
                        imageUrl = recipe.imageUrl,
                        mealPosition = mealPosition.toLong(),
                        servings = recipe.servings.toLong(),
                        calories = recipe.nutrients[0].amount * recipe.servings
                    )
                    newRecipeRecords = oldRecipeRecords + newRecipeRecord
                }

                // Update nutrient list, increase amount of every nutrient added before.
                oldNutrientRecords.forEach { nutrient ->
                    val recipeNutrientAmount = recipe.nutrients.firstOrNull {
                        it.name == nutrient.name
                    }?.amount ?: 0f
                    newNutrientRecords.add(
                        NutrientData(
                            name = nutrient.name,
                            amount = nutrient.amount + recipeNutrientAmount * recipe.servings,
                            unit = nutrient.unit
                        )
                    )
                }
                // Add new nutrients to nutrient list if it doesn't exist.
                recipe.nutrients.forEach { nutrient ->
                    if (oldNutrientRecords.firstOrNull {
                            it.name == nutrient.name
                        } == null) {
                        newNutrientRecords.add(
                            NutrientData(
                                name = nutrient.name,
                                amount = nutrient.amount * recipe.servings,
                                unit = nutrient.unit
                            )
                        )
                    }
                }

                newIntakeNutrition = intakeNutrition.copy(
                    nutrients = newNutrientRecords,
                    recipes = newRecipeRecords
                )
            } else {
                val newNutrientRecords = recipe.nutrients.map { nutrient ->
                    NutrientData(
                        name = nutrient.name,
                        amount = nutrient.amount * recipe.servings,
                        unit = nutrient.unit
                    )
                }
                newIntakeNutrition = IntakeNutrition(
                    date = date,
                    nutrients = newNutrientRecords,
                    recipes = listOf(
                        NutritionRecipeData(
                            id = recipe.id.toLong(),
                            name = recipe.name,
                            imageUrl = recipe.imageUrl,
                            mealPosition = mealPosition.toLong(),
                            servings = recipe.servings.toLong(),
                            calories = recipe.nutrients[RECIPE_NUTRIENT_CALORIES_INDEX].amount * recipe.servings
                        )
                    )
                )
            }

            transaction.set(
                collectionRef.document(documentId),
                newIntakeNutrition,
                SetOptions.merge()
            )
        }

        return@withContext try {
            transaction.await()
            Result.Success(Unit)
        } catch (e: FirebaseException) {
            Log.e(FIREBASE_TRANSACTION_EXCEPTION, "Update data failed with ", e)
            Result.Error(e)
        }
    }

    override suspend fun getDailyNutritionInfo(date: Date) = withContext(Dispatchers.IO) {
        val docRef = firestore.collection(USER_COLLECTION).document(auth.currentUser!!.uid)
        return@withContext try {
            val userInfoDeferred = async { docRef.get() }.await()
            val intakeNutritionDeferred = async {
                docRef.collection(USER_RECORD_SUB_COLLECTION)
                    .whereGreaterThanOrEqualTo(USER_RECORD_DATE_FIELD, date.startOfDay())
                    .whereLessThan(USER_RECORD_DATE_FIELD, date.endOfDay())
                    .get()
            }.await()
            val userInfo = userInfoDeferred.await().toObject(User::class.java) ?: User()
            val nutritionRecordDocument = intakeNutritionDeferred.await()
            val intakeNutrition = if (!nutritionRecordDocument.isEmpty) {
                nutritionRecordDocument.documents[0].toObject(IntakeNutrition::class.java)
            } else {
                null
            }
            val intakeNutrients = intakeNutrition?.nutrients ?: emptyList()
            Result.Success(
                DailyNutritionDisplayData(
                    targetCalories = userInfo.targetNutrition.calories.toInt(),
                    targetCarbRatio = userInfo.targetNutrition.carbRatio.toFloat(),
                    targetFatRatio = userInfo.targetNutrition.fatRatio.toFloat(),
                    targetProteinRatio = userInfo.targetNutrition.proteinRatio.toFloat(),
                    nutrients = intakeNutrients
                )
            )
        } catch (e: FirebaseException) {
            Log.e(FIREBASE_GET_DATA_EXCEPTION, "Get data failed with ", e)
            Result.Error(e)
        }
    }

    override fun getUserInfo() = callbackFlow {
        firestore.collection(USER_COLLECTION).document(auth.currentUser!!.uid)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful && task.result.exists()) {
                    val user = task.result.toObject(User::class.java)!!
                    channel.trySend(user)
                } else {
                    channel.trySend(User())
                }
            }
        awaitClose()
    }.flowOn(Dispatchers.IO)

    override fun getNutritionRecordsInAMonth(
        startDate: Date,
        endDate: Date
    ) = callbackFlow {
        val docRef = firestore.collection(USER_COLLECTION).document(auth.currentUser!!.uid)
        docRef.collection(USER_RECORD_SUB_COLLECTION)
            .whereGreaterThan(USER_RECORD_DATE_FIELD, startDate.startOfDay())
            .whereLessThan(USER_RECORD_DATE_FIELD, endDate.endOfDay())
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful && !task.result.isEmpty) {
                    val nutritionRecords = task.result.documents.map { snapshot ->
                        snapshot.toObject(IntakeNutrition::class.java) ?: IntakeNutrition()
                    }
                    channel.trySend(nutritionRecords)
                } else {
                    Log.e(FIREBASE_GET_DATA_EXCEPTION, "Get data failed with ", task.exception)
                    channel.trySend(emptyList())
                }
            }
        awaitClose()
    }.flowOn(Dispatchers.IO)

    override suspend fun saveRecipe(recipe: Recipe) = withContext(Dispatchers.IO) {
        val docRef = firestore.collection(USER_COLLECTION).document(auth.currentUser!!.uid)
        try {
            docRef.collection(USER_RECIPE_SUB_COLLECTION).document(recipe.recipeId.toString())
                .set(recipe)
                .await()
            Result.Success(Unit)
        } catch (e: FirebaseException) {
            Log.e(FIREBASE_EXCEPTION, "Update data failed with ", e)
            Result.Error(e)
        }
    }

    override fun getSavedRecipeById(recipeId: Int) = callbackFlow {
        val docRef = firestore.collection(USER_COLLECTION).document(auth.currentUser!!.uid)
        docRef.collection(USER_RECIPE_SUB_COLLECTION)
            .whereEqualTo(USER_RECIPE_ID_FIELD, recipeId)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful && !task.result.isEmpty) {
                    val recipe = task.result.documents[0].toObject(Recipe::class.java)
                    recipe?.let {
                        channel.trySend(it)
                    } ?: channel.trySend(Recipe())
                } else {
                    Log.e(FIREBASE_GET_DATA_EXCEPTION, "Get data failed with ", task.exception)
                    channel.trySend(Recipe())
                }
            }
        awaitClose()
    }.flowOn(Dispatchers.IO)

    override fun getSavedRecipeList() = callbackFlow {
        val docRef = firestore.collection(USER_COLLECTION).document(auth.currentUser!!.uid)
        val listenerRegistration = docRef.collection(USER_RECIPE_SUB_COLLECTION)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e(FIREBASE_EXCEPTION, "Get data failed with ", error)
                    channel.trySend(emptyList())
                    return@addSnapshotListener
                } else {
                    val recipes = if (snapshot != null && !snapshot.isEmpty) {
                        snapshot.documents.map { it.toObject(Recipe::class.java) ?: Recipe() }
                    } else {
                        emptyList()
                    }
                    channel.trySend(recipes)
                }
            }
        awaitClose { listenerRegistration.remove() }
    }.flowOn(Dispatchers.IO)
}
