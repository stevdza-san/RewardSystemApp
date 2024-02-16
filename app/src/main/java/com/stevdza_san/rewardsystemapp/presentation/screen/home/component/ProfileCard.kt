package com.stevdza_san.rewardsystemapp.presentation.screen.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.stevdza_san.rewardsystemapp.model.MyUser

@Composable
fun ProfileCard(
    modifier: Modifier = Modifier,
    myUser: MyUser,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(myUser.picture)
                .crossfade(true)
                .build(),
            contentDescription = "Profile Picture",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(CircleShape)
                .size(120.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = myUser.name,
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            modifier = Modifier.alpha(0.5f),
            text = myUser.email,
            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
        )
    }
}