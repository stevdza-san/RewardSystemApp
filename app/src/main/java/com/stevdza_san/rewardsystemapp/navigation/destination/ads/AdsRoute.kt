package com.stevdza_san.rewardsystemapp.navigation.destination.ads

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.stevdza_san.rewardsystemapp.navigation.Screen
import com.stevdza_san.rewardsystemapp.presentation.screen.ads.AdsScreen
import com.stevdza_san.rewardsystemapp.util.Constants.USER_ID_ARG

fun NavGraphBuilder.adsRoute(navigateBack: (String) -> Unit) {
    composable(
        route = Screen.Ads.route,
        arguments = listOf(navArgument(
            name = USER_ID_ARG
        ) {
            type = NavType.StringType
            defaultValue = ""
        })
    ) {
        val userId = it.arguments?.getString(USER_ID_ARG) ?: ""
        AdsScreen(
            userId = userId,
            onError = navigateBack,
            onSuccess = { navigateBack("Success!") }
        )
    }
}