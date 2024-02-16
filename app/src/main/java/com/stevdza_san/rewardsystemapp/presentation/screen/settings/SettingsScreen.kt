package com.stevdza_san.rewardsystemapp.presentation.screen.settings

import android.app.Activity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.stevdza_san.rewardsystemapp.presentation.screen.settings.component.SettingsView
import com.stevdza_san.rewardsystemapp.util.ads.AdConfiguration
import com.stevdza_san.rewardsystemapp.util.ads.MobileAdsConsentManager
import com.stevdza_san.rewardsystemapp.util.ads.detectAdConfiguration

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    adConsent: Boolean,
    onAdConsentChange: (Boolean) -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current as Activity
    val consentManager = remember { MobileAdsConsentManager.getInstance(context) }
    var adConfiguration by remember { mutableStateOf(detectAdConfiguration(context)) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Settings", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Back Arrow Icon"
                        )
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 24.dp)
                .padding(top = it.calculateTopPadding())
                .padding(bottom = it.calculateBottomPadding())
                .padding(horizontal = 24.dp)
        ) {
            SettingsView(
                title = "Ad Consent",
                subtitle = "You agree to participate in earning digital coins by viewing ads.",
                checked = if (consentManager.isPrivacyOptionsRequired)
                    adConfiguration == AdConfiguration.ALL else adConsent,
                onCheckedChange = { checked ->
                    if (consentManager.isPrivacyOptionsRequired) {
                        consentManager.showPrivacyOptionsForm(context) {
                            adConfiguration = detectAdConfiguration(context)
                        }
                    } else {
                        onAdConsentChange(checked)
                    }
                }
            )
        }
    }
}