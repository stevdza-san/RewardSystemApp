package com.stevdza_san.rewardsystemapp.presentation.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stevdza_san.rewardsystemapp.domain.DataStoreOperations
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStore: DataStoreOperations
): ViewModel() {
    val adConsent = dataStore.readAdConsentState()

    fun updateAdConsent(optIn: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.saveAdConsentState(optIn = optIn)
        }
    }
}