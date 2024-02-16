package com.stevdza_san.rewardsystemapp.navigation.destination.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.stevdza_san.rewardsystemapp.navigation.Screen
import com.stevdza_san.rewardsystemapp.presentation.component.AdConsentDialog
import com.stevdza_san.rewardsystemapp.presentation.screen.home.HomeScreen
import com.stevdza_san.rewardsystemapp.presentation.screen.home.HomeViewModel
import com.stevdza_san.rewardsystemapp.util.Constants.COINS_REWARD
import com.stevdza_san.rewardsystemapp.util.Constants.MAX_COINS
import com.stevdza_san.rewardsystemapp.util.RequestState
import com.stevdza_san.rewardsystemapp.util.ads.AdConfiguration
import com.stevdza_san.rewardsystemapp.util.ads.MobileAdsConsentManager
import com.stevdza_san.rewardsystemapp.util.ads.detectAdConfiguration
import com.stevdzasan.messagebar.rememberMessageBarState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun NavGraphBuilder.homeRoute(
    onWatchTheAdClick: (String) -> Unit,
    onSettingsClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    composable(route = Screen.Home.route) {
        val viewModel: HomeViewModel = hiltViewModel()
        val myUser by viewModel.myUser.collectAsStateWithLifecycle(RequestState.Idle)
        val adConsentState by viewModel.adConsent.collectAsStateWithLifecycle(false)
        val messageBarState = rememberMessageBarState()
        val scope = rememberCoroutineScope()
        val context = LocalContext.current
        var dialogOpened by remember { mutableStateOf(false) }

        val consentManager = remember { MobileAdsConsentManager.getInstance(context) }
        val adConfiguration by remember { mutableStateOf(detectAdConfiguration(context)) }

        if (dialogOpened) {
            AdConsentDialog(
                privacyOptionsRequired = consentManager.isPrivacyOptionsRequired,
                onNegativeClick = { dialogOpened = false },
                onPositiveClick = { dialogOpened = false },
                onDismiss = { dialogOpened = false }
            )
        }

        HomeScreen(
            myUser = myUser,
            messageBarState = messageBarState,
            onWatchTheAdClick = {
                if (consentManager.isPrivacyOptionsRequired) {
                    if (adConfiguration == AdConfiguration.ALL) {
                        if (myUser.isSuccess()) {
                            if ((myUser.getSuccessData().coins + COINS_REWARD) > MAX_COINS) {
                                messageBarState.addError(Exception("Maximum amount of Coins reached."))
                            } else {
                                onWatchTheAdClick(myUser.getSuccessData()._id.toHexString())
                            }
                        } else if (myUser.isError()) {
                            messageBarState.addError(Exception(myUser.getErrorMessage()))
                        }
                    } else {
                        dialogOpened = true
                    }
                } else {
                    if (adConsentState) {
                        if (myUser.isSuccess()) {
                            if ((myUser.getSuccessData().coins + COINS_REWARD) > MAX_COINS) {
                                messageBarState.addError(Exception("Maximum amount of Coins reached."))
                            } else {
                                onWatchTheAdClick(myUser.getSuccessData()._id.toHexString())
                            }
                        } else if (myUser.isError()) {
                            messageBarState.addError(Exception(myUser.getErrorMessage()))
                        }
                    } else {
                        dialogOpened = true
                    }
                }
            },
            onSettingsClick = onSettingsClick,
            onLogoutConfirmed = {
                scope.launch(Dispatchers.IO) {
                    withContext(Dispatchers.Main) {
                        messageBarState.addSuccess("Logging out...")
                    }
                    try {
                        val userCleared = viewModel.clearUser()
                        if (userCleared is RequestState.Success) {
                            withContext(Dispatchers.Main) {
                                delay(2000)
                                onLogoutClick()
                            }
                        } else if (userCleared is RequestState.Error) {
                            withContext(Dispatchers.Main) {
                                messageBarState.addError(Exception(userCleared.message))
                            }
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            messageBarState.addError(e)
                        }
                    }
                }
            }
        )
    }
}