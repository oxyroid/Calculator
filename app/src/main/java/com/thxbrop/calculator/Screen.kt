package com.thxbrop.calculator

sealed class Screen(val route: String) {
    object Main : Screen("route-main")
}
