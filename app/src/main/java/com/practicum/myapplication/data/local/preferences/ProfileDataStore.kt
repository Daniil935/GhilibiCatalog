package com.practicum.myapplication.data.local.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.practicum.myapplication.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.profileDataStore by preferencesDataStore(
    name = "user_profile"
)

class ProfileDataStore(
    private val context: Context
) {
    private companion object {
        val FULL_NAME_KEY = stringPreferencesKey("full_name")
        val AVATAR_URI_KEY = stringPreferencesKey("avatar_uri")
        val PORTFOLIO_URL_KEY = stringPreferencesKey("portfolio_url")
        val FAVORITE_FILM_KEY = stringPreferencesKey("favorite_film")
        val FAVORITE_DIRECTOR_KEY = stringPreferencesKey("favorite_director")
    }

    val profile: Flow<UserProfile> =
        context.profileDataStore.data.map { preferences ->
            UserProfile(
                fullName = preferences[FULL_NAME_KEY] ?: "",
                avatarUri = preferences[AVATAR_URI_KEY] ?: "",
                portfolioUrl = preferences[PORTFOLIO_URL_KEY] ?: "",
                favoriteFilm = preferences[FAVORITE_FILM_KEY] ?: "",
                favoriteDirector = preferences[FAVORITE_DIRECTOR_KEY] ?: ""
            )
        }

    suspend fun saveProfile(profile: UserProfile) {
        context.profileDataStore.edit { preferences ->
            preferences[FULL_NAME_KEY] = profile.fullName
            preferences[AVATAR_URI_KEY] = profile.avatarUri
            preferences[PORTFOLIO_URL_KEY] = profile.portfolioUrl
            preferences[FAVORITE_FILM_KEY] = profile.favoriteFilm
            preferences[FAVORITE_DIRECTOR_KEY] = profile.favoriteDirector
        }
    }
}
