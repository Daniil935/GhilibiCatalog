package com.practicum.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.myapplication.data.cache.AppCache
import com.practicum.myapplication.data.local.preferences.FilterSettingsDataStore
import com.practicum.myapplication.domain.model.FilterSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FilterSettingsViewModel(
    private val dataStore: FilterSettingsDataStore,
    private val appCache: AppCache
) : ViewModel() {

    private val _settings = MutableStateFlow(FilterSettings())
    val settings: StateFlow<FilterSettings> = _settings.asStateFlow()

    init {
        viewModelScope.launch {
            dataStore.settings.collect { savedSettings ->
                _settings.value = savedSettings
                appCache.updateFilterSettings(savedSettings)
            }
        }
    }

    fun updateTitle(value: String) {
        _settings.update { it.copy(title = value) }
    }

    fun updateDirector(value: String) {
        _settings.update { it.copy(director = value) }
    }

    fun updateReleaseYear(value: String) {
        _settings.update { it.copy(releaseYear = value) }
    }

    fun saveSettings(onSaved: () -> Unit) {
        viewModelScope.launch {
            val currentSettings = _settings.value
            dataStore.saveSettings(currentSettings)
            appCache.updateFilterSettings(currentSettings)
            onSaved()
        }
    }

    fun clearSettings() {
        viewModelScope.launch {
            dataStore.clearSettings()
        }
    }
}