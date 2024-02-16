package com.stevdza_san.rewardsystemapp.presentation.screen.home

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.stevdza_san.rewardsystemapp.model.MyUser
import com.stevdza_san.rewardsystemapp.presentation.component.LogoutConfirmationDialog
import com.stevdza_san.rewardsystemapp.presentation.screen.home.component.ProfileCard
import com.stevdza_san.rewardsystemapp.util.RequestState
import com.stevdzasan.messagebar.ContentWithMessageBar
import com.stevdzasan.messagebar.MessageBarState

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    myUser: RequestState<MyUser>,
    messageBarState: MessageBarState,
    onWatchTheAdClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onLogoutConfirmed: () -> Unit
) {
    val menuItems = listOf("Logout", "Settings")
    var expanded by remember { mutableStateOf(false) }
    var logoutDialogOpened by remember { mutableStateOf(false) }

    if (logoutDialogOpened) {
        LogoutConfirmationDialog(
            onPositiveClick = {
                logoutDialogOpened = false
                onLogoutConfirmed()
            },
            onDismiss = { logoutDialogOpened = false }
        )
    }

    ContentWithMessageBar(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding(),
        messageBarState = messageBarState,
        errorMaxLines = 2
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(text = "Home", fontWeight = FontWeight.Bold)
                    },
                    actions = {
                        IconButton(onClick = { expanded = true }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "Vertical Menu Icon",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                menuItems.forEach { title ->
                                    DropdownMenuItem(
                                        onClick = {
                                            expanded = false
                                            if (title == "Settings") onSettingsClick()
                                            else logoutDialogOpened = true
                                        },
                                        text = {
                                            Text(text = title)
                                        }
                                    )
                                }
                            }
                        }
                    }
                )
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    text = { Text(text = "Watch the Ad") },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Checkmark Icon"
                        )
                    },
                    onClick = onWatchTheAdClick
                )
            }
        ) {
            if (myUser.isSuccess()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(
                            top = it.calculateTopPadding()
                        )
                ) {
                    ProfileCard(
                        modifier = Modifier.padding(top = 24.dp),
                        myUser = myUser.getSuccessData()
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = myUser.getSuccessData().coins.toString(),
                        fontSize = MaterialTheme.typography.displayLarge.fontSize,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .alpha(0.5f),
                        text = "Coins",
                        fontSize = MaterialTheme.typography.bodySmall.fontSize,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.weight(1f))
                }
            } else if (myUser.isError()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(all = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = myUser.getErrorMessage())
                }
            }
        }
    }
}