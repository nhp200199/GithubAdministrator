package com.phucnguyen.githubadministrator.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.phucnguyen.githubadministrator.features.userDetail.UserDetailVM
import com.phucnguyen.githubadministrator.features.userList.UserListScreenVM

@Composable
fun GithubAdministratorNavGraph(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = UserList
    ) {
        composable<UserDetail> { backStackEntry ->
            val userDetail = backStackEntry.toRoute<UserDetail>()
            UserDetailVM(
                userName = userDetail.userName,
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }

        composable<UserList> {
            UserListScreenVM(
                onNavigateToDetail = {
                    navController.navigate(UserDetail(it))
                }
            )
        }
    }
}