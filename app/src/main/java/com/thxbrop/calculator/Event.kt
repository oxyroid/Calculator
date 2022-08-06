package com.thxbrop.calculator

sealed class Event<out T> private constructor(
    private val data: T? = null
) {
    open var isHandled: Boolean = false

    inline fun handle(block: (T) -> Unit) {
        if (!isHandled) {
            isHandled = true
            block.invoke(peek())
        }
    }

    fun peek() = data!!

    class Handled<out T> : Event<T>() {
        override var isHandled: Boolean = true
    }

    class Regular<out T>(data: T) : Event<T>(data)
}

fun <T> eventOf(data: T) = Event.Regular(data)