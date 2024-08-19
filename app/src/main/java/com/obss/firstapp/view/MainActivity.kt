package com.obss.firstapp.view

import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.forEach
import androidx.navigation.NavController
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
                val navController = rememberNavController()
                MainScreen(navController, savedInstanceState)
            }
        }
    }
}

@Composable
fun MainScreen(
    navController: NavController,
    savedInstanceState: Bundle?,
) {
    Box(Modifier.fillMaxSize()) {
        if (savedInstanceState == null) {
            Handler(Looper.getMainLooper()).postDelayed({
                navController.navigate("home")
            }, SPLASH_DELAY_TIME)
        }

        BottomNavigationView(navController)
    }
}

@Composable
fun BottomNavigationView(navController: NavController) {
    val configuration = LocalConfiguration.current
    AndroidView(
        factory = { context ->
            BottomNavigationView(context).apply {
                inflateMenu(R.menu.bottom_menu)

                setOnItemSelectedListener {
                    when (it.itemId) {
                        R.id.bottom_home -> {
                            navController.navigate("home")
                            true
                        }
                        R.id.bottom_search -> {
                            navController.navigate("search")
                            true
                        }
                        R.id.bottom_favorite -> {
                            navController.navigate("favorite")
                            true
                        }
                        else -> {
                            false
                        }
                    }
                }
            /*layoutParams =
                BottomNavigationView.LayoutParams(
                    BottomNavigationView.LayoutParams.MATCH_PARENT,
                    resources.getDimension(R.dimen.bottom_menu_height).toInt(),
                )*/

                if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    layoutParams.height = resources.getDimension(R.dimen.bottom_menu_height_landscape).toInt()
                    menu.forEach { it.title = EMPTY }
                }
            }
        },
        update = { view ->
            val layoutParams = view.layoutParams

            if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                layoutParams.height = view.resources.getDimension(R.dimen.bottom_menu_height_landscape).toInt()
                view.menu.forEach { it.title = EMPTY }
            } else {
                layoutParams.height = view.resources.getDimension(R.dimen.bottom_menu_height).toInt()
            }
            view.layoutParams = layoutParams
        },
    )
}

private const val SPLASH_DELAY_TIME = 2000L
private const val EMPTY = ""
