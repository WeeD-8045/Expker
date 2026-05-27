package com.example.expker.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.expker.model.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserPreferencesRepository(private val context: Context) {

    private object PreferencesKeys {
        val USER_NAME = stringPreferencesKey("user_name")
        val PROFILE_PICTURE_URI = stringPreferencesKey("profile_picture_uri")
        val THEME_MODE = intPreferencesKey("theme_mode") // 0: System, 1: Light, 2: Dark
        val ACCENT_COLOR = longPreferencesKey("accent_color")
    }

    val userProfileFlow: Flow<UserProfile> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val name = preferences[PreferencesKeys.USER_NAME] ?: "Faiz"
            val uri = preferences[PreferencesKeys.PROFILE_PICTURE_URI]
            val themeMode = preferences[PreferencesKeys.THEME_MODE] ?: 0
            val accentColor = preferences[PreferencesKeys.ACCENT_COLOR] ?: 0xFF4ADE80L // Default AccentGreen
            UserProfile(name, uri, themeMode, accentColor)
        }

    suspend fun updateName(name: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_NAME] = name
        }
    }

    suspend fun updateProfilePicture(uri: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.PROFILE_PICTURE_URI] = uri
        }
    }

    suspend fun updateThemeMode(mode: Int) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.THEME_MODE] = mode
        }
    }

    suspend fun updateAccentColor(color: Long) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.ACCENT_COLOR] = color
        }
    }
}
