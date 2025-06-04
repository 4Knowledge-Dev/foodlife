package com.forknowledge.core.data

import android.util.Log
import com.forknowledge.core.common.extension.endOfDay
import com.forknowledge.core.common.extension.startOfDay
import com.forknowledge.feature.model.Nutrition
import com.forknowledge.feature.model.Record
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import java.util.Date
import javax.inject.Inject

const val USER_COLLECTION = "User"
const val USER_RECORD_SUB_COLLECTION = "record"
const val USER_TARGET_CALORIES_FIELD = "targetCalories"
const val USER_TARGET_CARBS_FIELD = "targetCarbs"
const val USER_TARGET_PROTEINS_FIELD = "targetProteins"
const val USER_TARGET_FATS_FIELD = "targetFats"
const val USER_RECORD_DATE_FIELD = "date"

const val FIREBASE_EXCEPTION = "FirebaseException"

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
                        channel.trySend(
                            snapshot.data?.let { data ->
                                Nutrition(
                                    targetCalories = data[USER_TARGET_CALORIES_FIELD] as Long,
                                    targetCarbs = data[USER_TARGET_CARBS_FIELD] as Long,
                                    targetProteins = data[USER_TARGET_PROTEINS_FIELD] as Long,
                                    targetFats = data[USER_TARGET_FATS_FIELD] as Long
                                )
                            }
                        )
                    } else {
                        Log.d("Firebase", "No data found.")
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
                    channel.trySend(Record())
                    return@addSnapshotListener
                }
                if (snapshot != null && !snapshot.isEmpty) {
                    val record = snapshot.documents[0].toObject(Record::class.java)
                    channel.trySend(record ?: Record())

                } else {
                    Log.d("Firebase", "No record found at date $date (real-time).")
                    channel.trySend(Record())
                }
            }

        awaitClose {
            listenerRegistration.remove()
        }
    }
        .flowOn(Dispatchers.IO)
        .conflate()
}
