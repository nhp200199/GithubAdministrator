package com.phucnguyen.githubadministrator.features.userList

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import com.phucnguyen.githubadministrator.R
import com.phucnguyen.githubadministrator.common.model.UserOverview
import com.phucnguyen.githubadministrator.dataTest.USER_LIST_MODEL

@Composable
fun UserListScreenVM(
    onNavigateToDetail: (String) -> Unit,
    viewModel: UserListViewModel = hiltViewModel(),
) {
    val userPagingState = viewModel.usersPaginatedFlow.collectAsLazyPagingItems()

    UserListScreen(
        userPagingState = userPagingState,
        onNavigateToDetail = onNavigateToDetail
    )
}

@Composable
fun UserListScreen(
    userPagingState: LazyPagingItems<UserOverview>,
    onNavigateToDetail: (String) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(
            8.dp
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .background(color = Color.White)
    ) {
        items(userPagingState.itemCount) { index ->
            userPagingState[index]?.let { user ->
                Log.d("Phuc", "itemCount = ${userPagingState.itemCount}")

                Card(
                    elevation = CardDefaults.elevatedCardElevation(
                        defaultElevation = 16.dp
                    ),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    modifier = Modifier
                        .clickable { onNavigateToDetail(user.userName) }
                ) {
                    Row(
                        modifier = Modifier
                            .padding(8.dp)
                    ) {
                        AsyncImage(
                            model = user.avatarUrl,
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
                                text = user.userName,
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
                                    text = user.landingPageUrl,
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
        }
    }
}

//@Composable
//@Preview
//private fun UserListScreenPreview() {
//    UserListScreen()
//}