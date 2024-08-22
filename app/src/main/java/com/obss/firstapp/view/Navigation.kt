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
import com.obss.firstapp.view.review.ReviewScreen
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
        composable(
            "review/{movieId}/{movieTitle}",
            arguments =
                listOf(
                    navArgument("movieId") { type = NavType.IntType },
                    navArgument("movieTitle") {
                        type =
                            NavType.StringType
                    },
                ),
        ) {
            val movieId = it.arguments?.getInt("movieId")
            val movieTitle = it.arguments?.getString("movieTitle")
            if (movieId != null && movieTitle != null) {
                ReviewScreen(navController, movieId, movieTitle)
            }
        }
    }
}
