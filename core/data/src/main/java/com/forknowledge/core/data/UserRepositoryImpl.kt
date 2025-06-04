package com.forknowledge.core.data

import android.util.Log
import com.forknowledge.core.common.extension.endOfDay
import com.forknowledge.core.common.extension.startOfDay
import com.forknowledge.feature.model.IntakeNutrition
import com.forknowledge.feature.model.User
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
const val USER_RECORD_DATE_FIELD = "date"

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
                    channel.trySend(IntakeNutrition())
                    return@addSnapshotListener
                }
                if (snapshot != null && !snapshot.isEmpty) {
                    try {
                        channel.trySend(
                            snapshot.documents[0].toObject(IntakeNutrition::class.java)
                                ?: IntakeNutrition()
                        )
                    } catch (e: Exception) {
                        Log.e(FIREBASE_EXCEPTION, "Get data failed with ", e)
                        channel.trySend(IntakeNutrition())
                    }
                } else {
                    Log.d(FIREBASE_GET_DATA_EXCEPTION, "No record found at date $date (real-time).")
                    channel.trySend(IntakeNutrition())
                }
            }

        awaitClose {
            listenerRegistration.remove()
        }
    }
        .flowOn(Dispatchers.IO)
        .conflate()
}
