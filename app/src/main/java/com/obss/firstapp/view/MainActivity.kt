package com.obss.firstapp.view

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.obss.firstapp.R
import com.obss.firstapp.ui.theme.MovieAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setContent {
            MovieAppTheme {
                val systemUiController = rememberSystemUiController()
                systemUiController.setSystemBarsColor(
                    color = Color.Gray,
                    darkIcons = false,
                )
                MainScreen()
                HideSystemBars()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val bottomNavItems = listOf("home", "favorite", "search")
    Scaffold(
        bottomBar = {
            val currentRoute =
                navController
                    .currentBackStackEntryAsState()
                    .value
                    ?.destination
                    ?.route
            if (currentRoute in bottomNavItems) {
                BottomNavigationView(navController = navController)
            }
        },
    ) { innerPadding ->
        Box(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
        ) {
            Navigation(navController = navController)
        }
    }
}

@Composable
fun BottomNavigationView(navController: NavController) {
    AndroidView(
        modifier = Modifier.fillMaxWidth(),
        factory = { context ->
            BottomNavigationView(context).apply {
                inflateMenu(R.menu.bottom_menu)
                setBackgroundColor(resources.getColor(R.color.gray_background, null))
                itemIconTintList = ColorStateList.valueOf(resources.getColor(R.color.white, null))
                itemTextColor = ColorStateList.valueOf(resources.getColor(R.color.white, null))
                itemActiveIndicatorColor = ColorStateList.valueOf(resources.getColor(R.color.red_bottom, null))
                setOnItemSelectedListener {
                    when (it.itemId) {
                        R.id.bottom_home -> {
                            navController.navigate(HOME)
                            true
                        }
                        R.id.bottom_search -> {
                            navController.navigate(SEARCH)
                            true
                        }
                        R.id.bottom_favorite -> {
                            navController.navigate(FAVORITE)
                            true
                        }
                        else -> {
                            false
                        }
                    }
                }
            }
        },
    )
}

@Composable
fun HideSystemBars() {
    val context = LocalView.current
    val window = (LocalView.current.context as? android.app.Activity)?.window

    DisposableEffect(Unit) {
        val windowInsetsController = window?.let { WindowCompat.getInsetsController(it, context) }
        windowInsetsController?.let {
            it.hide(WindowInsetsCompat.Type.systemBars())
            it.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        onDispose {
            windowInsetsController?.show(WindowInsetsCompat.Type.systemBars())
        }
    }
}

private const val HOME = "home"
private const val SEARCH = "search"
private const val FAVORITE = "favorite"
