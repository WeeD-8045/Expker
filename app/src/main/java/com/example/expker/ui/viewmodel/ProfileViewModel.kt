package com.example.expker.ui.viewmodel

import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.expker.data.UserPreferencesRepository
import com.example.expker.model.UserProfile
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UserPreferencesRepository(application)

    val userProfile: StateFlow<UserProfile> = repository.userProfileFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UserProfile("Faiz", null)
    )

    fun updateName(name: String) {
        viewModelScope.launch {
            repository.updateName(name)
        }
    }

    fun updateProfilePicture(uri: Uri) {
        viewModelScope.launch {
            // Persist permission so the image remains accessible after restart
            try {
                getApplication<Application>().contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (e: Exception) {
                // Ignore if it fails (e.g. if URI is already persistable or not needed)
            }
            repository.updateProfilePicture(uri.toString())
        }
    }

    fun updateThemeMode(mode: Int) {
        viewModelScope.launch {
            repository.updateThemeMode(mode)
        }
    }

    fun updateAccentColor(color: Long) {
        viewModelScope.launch {
            repository.updateAccentColor(color)
        }
    }
}
