package com.stevdza_san.rewardsystemapp.presentation.component

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

@Composable
fun SignInPromptsDialog(
    onPositiveClick: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        title = { Text(text = "Google Account sign-in prompts") },
        text = {
            Text(
                text = "To sign in with your Google Account you should " +
                        "enable a sign in prompts in the settings."
            )
        },
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = onDismiss,) {
                Text(text = "Close", color = MaterialTheme.colorScheme.outline)
            }
        },
        confirmButton = {
            TextButton(onClick = onPositiveClick) {
                Text(text = "Enable")
            }
        }
    )
}

@Composable
fun LogoutConfirmationDialog(
    onPositiveClick: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        title = {
            Text(text = "Sign Out")
        },
        text = {
            Text(
                text = "Are you sure you want to sign out from your account?"
            )
        },
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(
                onClick = onDismiss,
            ) {
                Text(text = "Cancel", color = MaterialTheme.colorScheme.outline)
            }
        },
        confirmButton = {
            TextButton(onClick = onPositiveClick) {
                Text(text = "Confirm")
            }
        }
    )
}

@Composable
fun AdConsentDialog(
    privacyOptionsRequired: Boolean,
    onNegativeClick: () -> Unit,
    onPositiveClick: () -> Unit,
    onDismiss: () -> Unit
) {
    val scrollState = rememberScrollState()
    AlertDialog(
        title = {
            Text(text = "Ad Consent Required")
        },
        text = {
            Text(
                modifier = Modifier
                    .verticalScroll(state = scrollState),
                text = buildAnnotatedString {
                    append("Before you can view an ad, we need your consent. Here's how you can enable it:")
                    append("\n\n")
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = MaterialTheme.typography.titleSmall.fontSize
                        )
                    ) {
                        append("Step 1: ")
                    }
                    append("Open up the Settings.")
                    append("\n")
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = MaterialTheme.typography.titleSmall.fontSize
                        )
                    ) {
                        append("Step 2: ")
                    }
                    append("Toggle the 'Ad Consent' Button.")
                    if (privacyOptionsRequired) {
                        append("\n")
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = MaterialTheme.typography.titleSmall.fontSize
                            )
                        ) {
                            append("Step 3: ")
                        }
                        append("After a Dialog pops up, click the 'Consent' Button.")
                    }
                })
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                enabled = scrollState.value >= scrollState.maxValue,
                onClick = onPositiveClick
            ) {
                Text(text = "Okay")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onNegativeClick,
            ) {
                Text(text = "Close", color = MaterialTheme.colorScheme.outline)
            }
        }
    )
}