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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.phucnguyen.githubadministrator.R
import com.phucnguyen.githubadministrator.common.model.UserDetail
import com.phucnguyen.githubadministrator.common.ui.component.ClickableUrlText
import com.phucnguyen.githubadministrator.common.ui.component.ErrorContainer
import com.phucnguyen.githubadministrator.common.ui.component.UserCard
import com.phucnguyen.githubadministrator.dataTest.USER_DETAIL_MODEL

@Composable
fun UserDetailVM(
    userName: String,
    onNavigateBack: () -> Unit,
    viewModel: UserDetailViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.getUserDetail(userName)
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Log.d("MovieDetail", "uiState = $uiState")
    UserDetailScreen(
        uiState = uiState,
        onRetry = { viewModel.getUserDetail(userName) },
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailScreen(
    uiState: UserDetailUIState,
    onRetry: () -> Unit,
    onNavigateBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        CenterAlignedTopAppBar(
            title = {
                Text(text = "User Detail")
            },
            navigationIcon = {
                IconButton(
                    onClick = onNavigateBack
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "back",
                    )
                }
            }
        )

        Box(
            modifier = Modifier
                .background(color = Color.White)
                .weight(99f)
                .fillMaxWidth()
        ) {
            when (uiState) {
                is UserDetailUIState.Error -> ErrorContainer(
                    message = uiState.message,
                    onRetry = onRetry,
                    modifier = Modifier.align(Alignment.Center)
                )
                is UserDetailUIState.Success -> UserDetailContainer(uiState.data)
                else -> CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
private fun UserDetailContainer(
    data: UserDetail,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp)
    ) {
        UserCard(
            avatarUrl = data.avatarUrl,
            userName = data.userName
        ) {
            if (data.location.isEmpty()) {
                Text(
                    text = "No location",
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                )
            } else {
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
                        text = data.location,
                        style = TextStyle(
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    )
                }
            }
        }

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

        ClickableUrlText(
            text = blogUrl
        )
    }
}

@Composable
@Preview
private fun UserDetailScreenPreview() {
    UserDetailScreen(
        uiState = UserDetailUIState.Success(USER_DETAIL_MODEL),
        onRetry = {},
        onNavigateBack = {}
//        uiState = UserDetailUIState.Error("Something went wrong")
//        uiState = UserDetailUIState.Initial
    )
}