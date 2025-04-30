package com.forknowledge.foodlife.initializer

import android.content.Context
import androidx.startup.Initializer
import com.forknowledge.foodlife.BuildConfig
import com.google.firebase.auth.FirebaseAuth

const val AUTH_EMULATOR_HOST = "10.0.2.2"
const val AUTH_EMULATOR_PORT = 9099

class FirebaseAuthInitializer: Initializer<FirebaseAuth> {

    override fun create(context: Context): FirebaseAuth {
        val firebaseAuth = FirebaseAuth.getInstance()
        if (BuildConfig.DEBUG) {
            firebaseAuth.useEmulator(AUTH_EMULATOR_HOST, AUTH_EMULATOR_PORT)
        }
        return firebaseAuth
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }
}
