package com.phucnguyen.githubadministrator.features.userDetail

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.phucnguyen.githubadministrator.R
import com.phucnguyen.githubadministrator.dataTest.USER_DETAIL_MODEL

@Composable
fun UserDetailVM(
    viewModel: UserDetailViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.getUserDetail("CapnKiefer")
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Log.d("MovieDetail", "uiState = $uiState")
    UserDetailScreen(uiState)
}

@Composable
fun UserDetailScreen(
    uiState: UserDetailUIState,
) {
    val data = if (uiState is UserDetailUIState.Success) uiState.data else USER_DETAIL_MODEL

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(horizontal = 8.dp)
    ) {
        UserInfo(
            userName = data.userName,
            avatarUrl = data.avatarUrl,
            location = data.location
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        FollowSection(
            followers = data.followers,
            following = data.following,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        BlogSection(
            blogUrl = data.landingPageUrl
        )
    }
}

@Composable
private fun UserInfo(
    userName: String,
    avatarUrl: String,
    location: String
) {
    Card(
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 16.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        modifier = Modifier
//            .background(color = Color.White)
//            .padding(8.dp)
//            .clip(RoundedCornerShape(12.dp))
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

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.LocationOn,
                        contentDescription = "location",
                        tint = Color.Gray,
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = location,
                        style = TextStyle(
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun FollowSection(
    modifier: Modifier = Modifier,
    followers: Int,
    following: Int
) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier
    ) {
        FollowItem(
            icon = ImageVector.vectorResource(id = R.drawable.ic_follower),
            contentDescription = "followers",
            value = followers.toString(),
            description = "Followers"
        )

        FollowItem(
            icon = ImageVector.vectorResource(id = R.drawable.ic_following),
            contentDescription = "following",
            value = following.toString(),
            description = "Following"
        )
    }
}

@Composable
private fun FollowItem(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    contentDescription: String,
    value: String,
    description: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(color = Color.LightGray)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = Color.Black,
                modifier = Modifier
                    .size(24.dp)
            )
        }

        Text(
            text = value,
            style = TextStyle(
                fontSize = 14.sp,
                color = Color.Black
            )
        )

        Text(
            text = description,
            style = TextStyle(
                fontSize = 14.sp,
                color = Color.Gray
            )
        )
    }
}

@Composable
private fun BlogSection(
    modifier: Modifier = Modifier,
    blogUrl: String
) {
    Column(modifier = modifier) {
        Text(
            text = "Blog",
            style = TextStyle(
                fontSize = 18.sp,
                color = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        //TODO: make this text clickable
        Text(
            text = blogUrl,
            style = TextStyle(
                fontSize = 14.sp,
                color = Color.Gray
            )
        )
    }
}

@Composable
@Preview
private fun UserDetailScreenPreview() {
    UserDetailScreen(
        uiState = UserDetailUIState.Success(USER_DETAIL_MODEL)
    )
}