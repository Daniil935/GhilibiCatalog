package com.practicum.myapplication.data.local.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.practicum.myapplication.domain.model.FilterSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(
    name = "filter_settings"
)

class FilterSettingsDataStore(
    private val context: Context
) {

    private companion object {
        val TITLE_KEY = stringPreferencesKey("title")
        val DIRECTOR_KEY = stringPreferencesKey("director")
        val RELEASE_YEAR_KEY = stringPreferencesKey("release_year")
    }

    // Получение сохранённых настроек
    val settings: Flow<FilterSettings> =
        context.dataStore.data.map { preferences ->
            FilterSettings(
                title = preferences[TITLE_KEY] ?: "",
                director = preferences[DIRECTOR_KEY] ?: "",
                releaseYear = preferences[RELEASE_YEAR_KEY] ?: ""
            )
        }

    // Сохранение настроек
    suspend fun saveSettings(settings: FilterSettings) {
        context.dataStore.edit { preferences ->
            preferences[TITLE_KEY] = settings.title
            preferences[DIRECTOR_KEY] = settings.director
            preferences[RELEASE_YEAR_KEY] = settings.releaseYear
        }
    }

    // Сброс настроек
    suspend fun clearSettings() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}