package com.thxbrop.calculator.screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

abstract class BaseViewModel<S, E>(emptyState: S) : ViewModel() {

    protected val _state = mutableStateOf(emptyState)
    protected val readable: S get() = _state.value
    val state: State<S> = _state

    abstract fun onEvent(event: E)
}