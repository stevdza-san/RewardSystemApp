package com.stevdza_san.rewardsystemapp.navigation.destination.auth

import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.stevdza_san.rewardsystemapp.model.PersistedUser
import com.stevdza_san.rewardsystemapp.navigation.Screen
import com.stevdza_san.rewardsystemapp.presentation.component.SignInPromptsDialog
import com.stevdza_san.rewardsystemapp.presentation.screen.auth.AuthScreen
import com.stevdza_san.rewardsystemapp.presentation.screen.auth.AuthViewModel
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.getUserFromTokenId
import com.stevdzasan.onetap.rememberOneTapSignInState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun NavGraphBuilder.authRoute(navigateToHome: () -> Unit) {
    composable(route = Screen.Auth.route) {
        var loading by remember { mutableStateOf(false) }
        var dialogOpened by remember { mutableStateOf(false) }
        val oneTapState = rememberOneTapSignInState()
        val messageBarState = rememberMessageBarState()

        val viewModel: AuthViewModel = hiltViewModel()
        val scope = rememberCoroutineScope()
        val context = LocalContext.current

        if (dialogOpened) {
            SignInPromptsDialog(
                onDismiss = { dialogOpened = false },
                onPositiveClick = {
                    val url = "https://myaccount.google.com/connections/settings"
                    val intent = CustomTabsIntent.Builder().build()
                    intent.launchUrl(context, Uri.parse(url))
                    dialogOpened = false
                }
            )
        }

        AuthScreen(
            loading = loading,
            oneTapState = oneTapState,
            messageBarState = messageBarState,
            onError = {
                messageBarState.addError(it)
                if (it.message != null && it.message!!.contains("Google Account not Found.")) {
                    dialogOpened = true
                }
                loading = false
            },
            onLoadingTrigger = {
                oneTapState.open()
                loading = true
            },
            onTokenIdReceived = { tokenId ->
                val user = getUserFromTokenId(tokenId = tokenId)
                if (user != null) {
                    viewModel.signInWithMongoAtlas(
                        tokenId = tokenId,
                        onSuccess = {
                            scope.launch(Dispatchers.Main) {
                                withContext(Dispatchers.IO) {
                                    val userSaved = viewModel.persistTheUser(
                                        persistedUser = PersistedUser(
                                            name = user.givenName.toString(),
                                            email = user.email.toString(),
                                            picture = user.picture.toString(),
                                        )
                                    )
                                    withContext(Dispatchers.Main) {
                                        if (userSaved.isSuccess()) {
                                            messageBarState.addSuccess("Successfully Authenticated!")
                                            loading = false
                                            delay(2000)
                                            navigateToHome()
                                        } else if (userSaved.isError()) {
                                            messageBarState.addError(
                                                Exception(userSaved.getErrorMessage())
                                            )
                                            loading = false
                                        }
                                    }
                                }
                            }
                        },
                        onError = { exception ->
                            messageBarState.addError(exception)
                            loading = false
                        }
                    )
                } else {
                    messageBarState.addError(Exception("Invalid User from tokenId. Report this problem."))
                    loading = false
                }
            }
        )
    }
}