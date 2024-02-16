package com.stevdza_san.rewardsystemapp.presentation.screen.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.stevdza_san.rewardsystemapp.R
import com.stevdza_san.rewardsystemapp.presentation.screen.auth.component.GoogleButton
import com.stevdza_san.rewardsystemapp.util.Constants.CLIENT_ID
import com.stevdzasan.messagebar.ContentWithMessageBar
import com.stevdzasan.messagebar.MessageBarState
import com.stevdzasan.onetap.OneTapSignInState
import com.stevdzasan.onetap.OneTapSignInWithGoogle

@Composable
fun AuthScreen(
    loading: Boolean,
    oneTapState: OneTapSignInState,
    messageBarState: MessageBarState,
    onLoadingTrigger: () -> Unit,
    onError: (Exception) -> Unit,
    onTokenIdReceived: (String) -> Unit
) {
    OneTapSignInWithGoogle(
        state = oneTapState,
        clientId = CLIENT_ID,
        onTokenIdReceived = onTokenIdReceived,
        onDialogDismissed = { onError(Exception(it)) }
    )

    ContentWithMessageBar(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding(),
        messageBarState = messageBarState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 24.dp)
                .padding(bottom = 102.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.size(100.dp),
                painter = painterResource(id = R.drawable.google_logo),
                contentDescription = "Logo Image"
            )
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "WELCOME BACK",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(0.6f),
                text = "Please sign in to continue.",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                textAlign = TextAlign.Center
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .padding(horizontal = 40.dp)
            .padding(bottom = 24.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        GoogleButton(
            modifier = Modifier.height(54.dp),
            loadingState = loading,
            shape = RoundedCornerShape(size = 4.dp),
            onClick = onLoadingTrigger
        )
    }
}