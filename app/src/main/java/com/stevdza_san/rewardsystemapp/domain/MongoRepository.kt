package com.stevdza_san.rewardsystemapp.domain

import com.stevdza_san.rewardsystemapp.model.MyUser
import com.stevdza_san.rewardsystemapp.util.RequestState
import kotlinx.coroutines.flow.Flow

interface MongoRepository {
    fun configureTheRealm(): RequestState<Unit>
    fun getUser(): Flow<RequestState<MyUser>>
    suspend fun reset()
}