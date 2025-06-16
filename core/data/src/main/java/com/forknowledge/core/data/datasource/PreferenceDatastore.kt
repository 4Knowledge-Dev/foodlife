package com.forknowledge.core.data.datasource

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.forknowledge.feature.model.userdata.UserToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

object SettingsKeys {
    val USERNAME = stringPreferencesKey("username")
    val HASH_KEY = stringPreferencesKey("hash_key")
}

val Context.dataStore by preferencesDataStore(name = "settings")

class PreferenceDatastore @Inject constructor(
    @ApplicationContext private val context: Context
) {

    suspend fun saveUserToken(
        username: String,
        hashKey: String
    ) {
        context.dataStore.edit { preference ->
            preference[SettingsKeys.USERNAME] = username
            preference[SettingsKeys.HASH_KEY] = hashKey
        }
    }

    suspend fun getUserToken() = context.dataStore.data
        .map { preference ->
            UserToken(
                username = preference[SettingsKeys.USERNAME] ?: "",
                hashKey = preference[SettingsKeys.HASH_KEY] ?: ""
            )
        }.first()
}
