package com.phucnguyen.githubadministrator.common.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.phucnguyen.githubadministrator.R

@Composable
fun UserCard(
    modifier: Modifier = Modifier,
    avatarUrl: String,
    userName: String,
    subHeader: @Composable () -> Unit
) {
    Card(
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 16.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
        ) {
            AsyncImage(
                model = avatarUrl,
                contentDescription = "user avatar",
                error = painterResource(id = R.drawable.ic_launcher_background),
                placeholder = painterResource(id = R.drawable.ic_launcher_background),
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
            )

            Column(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
            ) {
                Text(
                    text = userName,
                    style = TextStyle(
                        fontSize = 18.sp
                    )
                )

                Spacer(
                    modifier = Modifier
                        .height(8.dp)
                )

                Spacer(
                    modifier = Modifier
                        .height(1.dp)
                        .fillMaxWidth()
                        .background(color = Color.Gray)
                )

                Spacer(
                    modifier = Modifier
                        .height(8.dp)
                )

                subHeader()
            }
        }
    }
}