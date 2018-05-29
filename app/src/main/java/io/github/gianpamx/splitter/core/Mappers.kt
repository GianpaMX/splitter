package io.github.gianpamx.splitter.core

import kotlin.math.roundToInt

fun Double.toCents() = try {
    (this * 100.0).roundToInt()
} catch (t: Throwable) {
    0
}

fun Int.toAmount(): Double = this.toDouble() / 100.0
