package com.thxbrop.calculator

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.thxbrop.calculator.screen.main.MainScreen

@Composable
fun CalculatorNavHost() {
    val navController = rememberNavController()
    val systemUiController = rememberSystemUiController()
    val navigationBarColor = MaterialTheme.colorScheme.surface
    val useDarkIcon = !isSystemInDarkTheme()

    LaunchedEffect(useDarkIcon) {
        systemUiController.setStatusBarColor(
            color = Color.Transparent,
            darkIcons = useDarkIcon
        )
        systemUiController.setNavigationBarColor(
            color = navigationBarColor,
            darkIcons = useDarkIcon
        )
    }
    NavHost(
        navController = navController,
        startDestination = Screen.Main.route,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding(),

        ) {
        composable(Screen.Main.route) {
            MainScreen()
        }
    }
}