package com.thxbrop.calculator.screen.main

import com.thxbrop.calculator.Event

data class MainState(
    val formula: List<String> = listOf("0"),
    val pre: List<String> = emptyList(),
    val message: Event<String> = Event.Handled(),
    val scroll: Boolean = false
)