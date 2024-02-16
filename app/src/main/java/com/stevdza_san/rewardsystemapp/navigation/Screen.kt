package com.stevdza_san.rewardsystemapp.navigation

import com.stevdza_san.rewardsystemapp.util.Constants.USER_ID_ARG

sealed class Screen(val route: String) {
    data object Auth : Screen(route = "auth_screen")
    data object Home : Screen(route = "home_screen")
    data object Settings : Screen(route = "settings_screen")
    data object Ads : Screen(route = "ads_screen/{${USER_ID_ARG}}") {
        fun passArgument(userId: String) = "ads_screen/$userId"
    }
}