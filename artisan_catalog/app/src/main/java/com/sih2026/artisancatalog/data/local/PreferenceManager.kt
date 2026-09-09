package com.sih2026.artisancatalog.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "artisan_settings")

class PreferenceManager(private val context: Context) {

    private val KEY_LANGUAGE = stringPreferencesKey("app_language")
    private val KEY_DEMO_MODE = booleanPreferencesKey("demo_mode_active")
    private val KEY_ARTISAN_NAME = stringPreferencesKey("artisan_name")
    private val KEY_SERVER_URL = stringPreferencesKey("server_url")

    val selectedLanguage: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[KEY_LANGUAGE] ?: "en"
    }

    val isDemoModeActive: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[KEY_DEMO_MODE] ?: true
    }

    val artisanName: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[KEY_ARTISAN_NAME] ?: "Artisan"
    }

    val serverUrl: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[KEY_SERVER_URL] ?: "http://10.10.159.148:3001/"
    }

    suspend fun setServerUrl(url: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_SERVER_URL] = url
        }
    }

    suspend fun setLanguage(langCode: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_LANGUAGE] = langCode
        }
    }

    suspend fun setDemoMode(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[KEY_DEMO_MODE] = enabled
        }
    }

    suspend fun setArtisanName(name: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_ARTISAN_NAME] = name
        }
    }
}
