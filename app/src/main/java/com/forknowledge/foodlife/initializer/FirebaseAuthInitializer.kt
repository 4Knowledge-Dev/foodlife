package com.forknowledge.foodlife.initializer

import android.content.Context
import androidx.startup.Initializer
import com.forknowledge.foodlife.BuildConfig
import com.google.firebase.auth.FirebaseAuth

class FirebaseAuthInitializer : Initializer<FirebaseAuth> {

    override fun create(context: Context): FirebaseAuth {
        val firebaseAuth = FirebaseAuth.getInstance()
        if (BuildConfig.DEBUG) {
            firebaseAuth.useEmulator(
                EmulatorDebug.EMULATOR_HOST,
                EmulatorDebug.AUTH_EMULATOR_PORT
            )
        }
        return firebaseAuth
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }
}
