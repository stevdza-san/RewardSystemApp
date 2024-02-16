package com.stevdza_san.rewardsystemapp.navigation

import android.app.Activity
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.stevdza_san.rewardsystemapp.data.MongoDB
import com.stevdza_san.rewardsystemapp.navigation.destination.ads.adsRoute
import com.stevdza_san.rewardsystemapp.navigation.destination.auth.authRoute
import com.stevdza_san.rewardsystemapp.navigation.destination.home.homeRoute
import com.stevdza_san.rewardsystemapp.navigation.destination.settings.settingsRoute
import kotlinx.coroutines.launch

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Auth.route
) {
    val scope = rememberCoroutineScope()
    val activity = LocalContext.current as Activity
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        authRoute(navigateToHome = { navController.navigate(Screen.Home.route) })
        homeRoute(
            onWatchTheAdClick = { navController.navigate(Screen.Ads.passArgument(it)) },
            onSettingsClick = {
                navController.navigate(Screen.Settings.route)
            },
            onLogoutClick = {
                navController.navigate(Screen.Auth.route) {
                    launchSingleTop = true
                }
                scope.launch { MongoDB.reset() }
            }
        )
        settingsRoute(onBackClick = { navController.popBackStack() })
        adsRoute(
            navigateBack = {
                Toast.makeText(
                    activity,
                    it,
                    Toast.LENGTH_LONG
                ).show()
                navController.popBackStack()
            }
        )
    }
}