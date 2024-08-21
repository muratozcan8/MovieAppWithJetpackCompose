package com.obss.firstapp.view

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.obss.firstapp.view.detail.DetailScreen
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
        composable("detail/{movieId}", arguments = listOf(navArgument("movieId") { type = NavType.IntType })) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId")
            if (movieId != null) {
                DetailScreen(navController, movieId)
            }
        }
    }
}
