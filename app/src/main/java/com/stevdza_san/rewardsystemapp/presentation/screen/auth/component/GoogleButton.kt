package com.stevdza_san.rewardsystemapp.presentation.screen.auth.component

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.stevdza_san.rewardsystemapp.R

@Composable
fun GoogleButton(
    modifier: Modifier = Modifier,
    loadingState: Boolean = false,
    primaryText: String = "Sign in with Google",
    secondaryText: String = "Please wait...",
    icon: Int = R.drawable.google_logo,
    shape: Shape = Shapes().extraSmall,
    textColor: Color = MaterialTheme.colorScheme.onBackground,
    backgroundColor: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
    progressIndicatorColor: Color = MaterialTheme.colorScheme.primary,
    onClick: () -> Unit
) {
    var buttonText by remember { mutableStateOf(primaryText) }
    LaunchedEffect(key1 = loadingState) {
        buttonText = if (loadingState) secondaryText else primaryText
    }

    Surface(
        modifier = modifier
            .clickable(enabled = !loadingState) { onClick() }
            .clip(shape),
        shape = shape,
        color = backgroundColor
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(
                    animationSpec = tween(durationMillis = 200)
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = "Google Logo",
                tint = Color.Unspecified
            )
            Text(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .animateContentSize(
                        animationSpec = tween(durationMillis = 200)
                    ),
                text = buttonText,
                color = textColor,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize
            )
            if (loadingState) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .size(16.dp),
                    strokeWidth = 2.dp,
                    color = progressIndicatorColor
                )
            }
        }
    }
}