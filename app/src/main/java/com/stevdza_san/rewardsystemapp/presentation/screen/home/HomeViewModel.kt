package com.stevdza_san.rewardsystemapp.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stevdza_san.rewardsystemapp.data.MongoDB
import com.stevdza_san.rewardsystemapp.domain.DataStoreOperations
import com.stevdza_san.rewardsystemapp.model.MyUser
import com.stevdza_san.rewardsystemapp.util.RequestState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dataStore: DataStoreOperations
): ViewModel() {
    private var _myUser: MutableStateFlow<RequestState<MyUser>> =
        MutableStateFlow(RequestState.Idle)
    val myUser: StateFlow<RequestState<MyUser>> = _myUser

    val adConsent = dataStore.readAdConsentState()

    init {
        MongoDB.configureTheRealm()
        getMyUser()
    }

    private fun getMyUser() {
        viewModelScope.launch(Dispatchers.Main) {
            MongoDB.getUser().collectLatest {
                _myUser.value = it
            }
        }
    }

    suspend fun clearUser() = dataStore.clearUser()
}