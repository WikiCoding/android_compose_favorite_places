package com.wikicoding.favoriteplaces.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.wikicoding.favoriteplaces.uiscreens.AddView
import com.wikicoding.favoriteplaces.uiscreens.EditView
import com.wikicoding.favoriteplaces.uiscreens.HomeView
import com.wikicoding.favoriteplaces.viewmodel.MainViewModel

@Composable
fun Navigation(
    navHostController: NavHostController = rememberNavController(),
    mainViewModel: MainViewModel = viewModel()
) {
    NavHost(navController = navHostController, startDestination = Screen.HomeScreen.route) {
        composable(Screen.HomeScreen.route) {
            HomeView(navController = navHostController, mainViewModel = mainViewModel)
        }
        composable(Screen.AddScreen.route) {
            AddView(navHostController = navHostController, mainViewModel = mainViewModel)
        }
        composable(Screen.EditScreen.route + "/{id}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.IntType
                    defaultValue = 0
                    nullable = false
                }
            )
        ) {
            val id = if (it.arguments != null) it.arguments!!.getInt("id") else 0
            EditView(navHostController = navHostController, id = id, mainViewModel = mainViewModel)
        }
    }
}