package com.wikicoding.favoriteplaces.navigation

sealed class Screen (val route: String){
    object HomeScreen : Screen("home_screen")
    object AddScreen : Screen("add_screen")
    object EditScreen : Screen("edit_screen")
}