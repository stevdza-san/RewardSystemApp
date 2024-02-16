package com.stevdza_san.rewardsystemapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.ads.MobileAds
import com.stevdza_san.rewardsystemapp.domain.DataStoreOperations
import com.stevdza_san.rewardsystemapp.model.PersistedUser
import com.stevdza_san.rewardsystemapp.navigation.Screen
import com.stevdza_san.rewardsystemapp.navigation.SetupNavGraph
import com.stevdza_san.rewardsystemapp.ui.theme.RewardSystemAppTheme
import com.stevdza_san.rewardsystemapp.util.ads.MobileAdsConsentManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var isMobileAdsInitializeCalled = AtomicBoolean(false)
    private lateinit var mobileAdsConsentManager: MobileAdsConsentManager

    @Inject
    lateinit var dataStore: DataStoreOperations

    private var appReady = false

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                Color.Transparent.toArgb(),
                Color.Transparent.toArgb()
            ),
            navigationBarStyle = SystemBarStyle.light(
                Color.Transparent.toArgb(),
                Color.Transparent.toArgb()
            )
        )
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition { !appReady }

        getConsent()

        setContent {
            RewardSystemAppTheme {
                val scope = rememberCoroutineScope()
                val navController = rememberNavController()
                var persistedUser by remember { mutableStateOf<PersistedUser?>(null) }

                LaunchedEffect(key1 = Unit) {
                    val deferredUser = async { dataStore.readPersistedUser().firstOrNull() }
                    deferredUser.invokeOnCompletion {
                        persistedUser = deferredUser.getCompleted()
                        scope.launch {
                            delay(500)
                            appReady = true
                        }
                    }
                }

                SetupNavGraph(
                    navController = navController,
                    startDestination = if (persistedUser != null) Screen.Home.route
                    else Screen.Auth.route
                )
            }
        }
    }

    private fun getConsent() {
        mobileAdsConsentManager = MobileAdsConsentManager.getInstance(applicationContext)
        mobileAdsConsentManager.gatherConsent(this) { error ->
            if (error != null) {
                Log.d("AdConsent", "${error.errorCode}: ${error.message}")
            }

            if (mobileAdsConsentManager.canRequestAds) {
                initializeMobileAdsSdk()
            }
        }

        if (mobileAdsConsentManager.canRequestAds) {
            initializeMobileAdsSdk()
        }
    }

    private fun initializeMobileAdsSdk() {
        if (isMobileAdsInitializeCalled.getAndSet(true)) return
        MobileAds.initialize(this)
    }
}