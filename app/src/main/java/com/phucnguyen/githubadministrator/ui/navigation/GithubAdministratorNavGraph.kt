package com.phucnguyen.githubadministrator.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.phucnguyen.githubadministrator.features.userDetail.UserDetailVM
import com.phucnguyen.githubadministrator.features.userList.UserListScreenVM

@Composable
fun GithubAdministratorNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = UserList
    ) {
        composable<UserDetail> { backStackEntry ->
            val userDetail = backStackEntry.toRoute<UserDetail>()
            UserDetailVM(
                userName = userDetail.userName
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