package com.obss.firstapp.view

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.obss.firstapp.view.favorite.FavoriteScreen
import com.obss.firstapp.view.home.HomeScreen
import com.obss.firstapp.view.search.SearchScreen

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController)
        }
        composable("search") {
            SearchScreen(navController)
        }
        composable("favorite") {
            FavoriteScreen(navController)
        }
    }
}
