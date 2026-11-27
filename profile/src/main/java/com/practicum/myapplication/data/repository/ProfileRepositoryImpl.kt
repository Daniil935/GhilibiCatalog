package com.practicum.myapplication.data.repository

import com.practicum.myapplication.data.local.preferences.ProfileDataStore
import com.practicum.myapplication.domain.model.UserProfile
import com.practicum.myapplication.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow

class ProfileRepositoryImpl(
    private val dataStore: ProfileDataStore
) : ProfileRepository {

    override val profile: Flow<UserProfile> = dataStore.profile

    override suspend fun saveProfile(profile: UserProfile) {
        dataStore.saveProfile(profile)
    }
}