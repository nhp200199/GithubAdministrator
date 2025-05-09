package com.phucnguyen.githubadministrator.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.phucnguyen.githubadministrator.ui.navigation.GithubAdministratorNavGraph
import com.phucnguyen.githubadministrator.ui.theme.GithubAdministratorTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            GithubAdministratorTheme {
                Scaffold { paddingValues ->
                    GithubAdministratorNavGraph(
                        modifier = Modifier.padding(paddingValues)
                    )
                }
            }
        }
    }
}