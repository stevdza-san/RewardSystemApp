package com.stevdza_san.rewardsystemapp.presentation.screen.ads

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.gms.ads.rewarded.ServerSideVerificationOptions
import com.stevdza_san.rewardsystemapp.util.Constants.TEST_REWARD_AD

@Composable
fun AdsScreen(
    userId: String,
    onError: (String) -> Unit,
    onSuccess: () -> Unit,
) {
    val context = LocalContext.current as Activity
    var rewardedAd: RewardedAd? by remember { mutableStateOf(null) }

    LaunchedEffect(key1 = Unit) {
        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(
            context,
            TEST_REWARD_AD,
            adRequest, object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    rewardedAd = null
                    onError(adError.message)
                }

                override fun onAdLoaded(ad: RewardedAd) {
                    rewardedAd = ad
                    if (userId.isNotEmpty()) {
                        val options = ServerSideVerificationOptions.Builder()
                            .setUserId(userId)
                            .setCustomData(rewardedAd!!.adUnitId)
                            .build()
                        rewardedAd!!.setServerSideVerificationOptions(options)
                    } else {
                        onError("UserId is empty.")
                    }
                }
            }
        )
    }

    LaunchedEffect(key1 = rewardedAd) {
        rewardedAd?.let { it.show(context) { onSuccess() } }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .padding(top = 12.dp),
            text = "Please wait.",
            color = MaterialTheme.colorScheme.onSurface
        )
    }

}