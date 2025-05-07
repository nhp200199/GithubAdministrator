package com.phucnguyen.githubadministrator.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.phucnguyen.githubadministrator.features.userDetail.UserDetailVM

@Composable
fun GithubAdministratorNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = UserDetail
    ) {
        composable<UserDetail> {
            UserDetailVM()
        }
    }
}