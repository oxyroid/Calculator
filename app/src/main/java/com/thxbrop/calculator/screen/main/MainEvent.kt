package com.thxbrop.calculator.screen.main

sealed class MainEvent {
    data class Append(val s: String) : MainEvent()
    object Drop : MainEvent()
    object Clear : MainEvent()
    object Percent : MainEvent()
    object Decimal : MainEvent()
    object Calculate : MainEvent()
}
