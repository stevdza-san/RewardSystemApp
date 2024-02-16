package com.stevdza_san.rewardsystemapp.navigation.destination.settings

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.stevdza_san.rewardsystemapp.navigation.Screen
import com.stevdza_san.rewardsystemapp.presentation.screen.settings.SettingsScreen
import com.stevdza_san.rewardsystemapp.presentation.screen.settings.SettingsViewModel

fun NavGraphBuilder.settingsRoute(onBackClick: () -> Unit) {
    composable(route = Screen.Settings.route) {
        val viewModel: SettingsViewModel = hiltViewModel()
        val adConsent by viewModel.adConsent.collectAsStateWithLifecycle(initialValue = false)

        SettingsScreen(
            adConsent = adConsent,
            onAdConsentChange = { viewModel.updateAdConsent(optIn = it) },
            onBackClick = onBackClick
        )
    }
}