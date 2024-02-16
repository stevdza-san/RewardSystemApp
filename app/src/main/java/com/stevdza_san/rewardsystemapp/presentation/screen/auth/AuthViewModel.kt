package com.stevdza_san.rewardsystemapp.presentation.screen.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stevdza_san.rewardsystemapp.data.MongoDB
import com.stevdza_san.rewardsystemapp.domain.DataStoreOperations
import com.stevdza_san.rewardsystemapp.model.PersistedUser
import com.stevdza_san.rewardsystemapp.util.Constants.APP_ID
import com.stevdza_san.rewardsystemapp.util.RequestState
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.Credentials
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val dataStore: DataStoreOperations
): ViewModel() {

    suspend fun persistTheUser(persistedUser: PersistedUser): RequestState<Unit> {
        return dataStore.persistTheUser(persistedUser)
    }

    fun signInWithMongoAtlas(
        tokenId: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val loggedInWithAtlas = App.create(APP_ID).login(
                    Credentials.jwt(jwtToken = tokenId)
                ).loggedIn
                val realmConfigured = MongoDB.configureTheRealm()
                if (realmConfigured.isSuccess()) {
                    if (loggedInWithAtlas) {
                        onSuccess()
                    } else {
                        onError(Exception("Failed to log in."))
                    }
                } else if (realmConfigured.isError()) {
                    onError(Exception(realmConfigured.getErrorMessage()))
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError(e)
                }
            }
        }
    }
}