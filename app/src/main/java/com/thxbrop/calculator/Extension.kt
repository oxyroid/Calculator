package com.thxbrop.calculator

fun <R> Boolean.ifTrue(block: () -> R): R? {
    return if (this) block.invoke()
    else null
}
fun <R> Boolean.ifFalse(block: () -> R): R? {
    return if (!this) block.invoke()
    else null
}