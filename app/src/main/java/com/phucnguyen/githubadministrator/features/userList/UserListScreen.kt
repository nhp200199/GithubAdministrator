package com.phucnguyen.githubadministrator.features.userList

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.phucnguyen.githubadministrator.common.model.UserOverview
import com.phucnguyen.githubadministrator.common.ui.component.ClickableUrlText
import com.phucnguyen.githubadministrator.common.ui.component.ErrorContainer
import com.phucnguyen.githubadministrator.common.ui.component.UserCard

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListScreen(
    userPagingState: LazyPagingItems<UserOverview>,
    onNavigateToDetail: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        CenterAlignedTopAppBar(
            title = {
                Text(text = "Github Users")
            }
        )

        Box(
            modifier = Modifier
                .background(color = Color.White)
                .weight(99f)
                .fillMaxWidth()
        ) {
            val refreshLoadState = userPagingState.loadState.refresh

            when(refreshLoadState) {
                is LoadState.Error -> ErrorContainer(
                    message = refreshLoadState.error.message ?: "",
                    onRetry = { userPagingState.retry() },
                    modifier = Modifier
                        .align(Alignment.Center)
                )
                LoadState.Loading -> CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                )
                is LoadState.NotLoading -> LazyColumn(
                    contentPadding = PaddingValues(
                        8.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .background(color = Color.White)
                ) {
                    items(userPagingState.itemCount) { index ->
                        userPagingState[index]?.let { user ->
                            UserCard(
                                avatarUrl = user.avatarUrl,
                                userName = user.userName,
                                modifier = Modifier
                                    .clickable { onNavigateToDetail(user.userName) }
                            ) {
                                ClickableUrlText(
                                    text = user.landingPageUrl
                                )
                            }
                        }
                    }

                    item {
                        val appendLoadState = userPagingState.loadState.append
                        when (appendLoadState) {
                            is LoadState.Error -> ErrorContainer(
                                message = appendLoadState.error.message ?: "",
                                onRetry = { userPagingState.retry() }
                            )
                            LoadState.Loading -> CircularProgressIndicator()
                            is LoadState.NotLoading -> Unit
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