package com.forknowledge.foodlife.initializer

import android.content.Context
import androidx.startup.Initializer
import com.forknowledge.foodlife.BuildConfig
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class FirestoreInitializer : Initializer<FirebaseFirestore> {

    override fun create(context: Context): FirebaseFirestore {
        val firestore = Firebase.firestore
        if (BuildConfig.DEBUG) {
            /*firestore.useEmulator(
                EmulatorDebug.EMULATOR_HOST,
                EmulatorDebug.FIREBASE_EMULATOR_PORT
            )

            firestore.firestoreSettings = firestoreSettings {
                isPersistenceEnabled = false
            }*/
        }
        return firestore
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }
}
