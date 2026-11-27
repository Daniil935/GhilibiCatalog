package com.practicum.myapplication.domain.repository

import com.practicum.myapplication.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    val profile: Flow<UserProfile>

    suspend fun saveProfile(profile: UserProfile)
}
