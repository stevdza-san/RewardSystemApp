package com.stevdza_san.rewardsystemapp.domain

import com.stevdza_san.rewardsystemapp.model.PersistedUser
import com.stevdza_san.rewardsystemapp.util.RequestState
import kotlinx.coroutines.flow.Flow

interface DataStoreOperations {
    suspend fun persistTheUser(persistedUser: PersistedUser): RequestState<Unit>
    suspend fun saveAdConsentState(optIn: Boolean): RequestState<Unit>
    suspend fun clearUser(): RequestState<Unit>
    fun readPersistedUser(): Flow<PersistedUser?>
    fun readAdConsentState(): Flow<Boolean>
}