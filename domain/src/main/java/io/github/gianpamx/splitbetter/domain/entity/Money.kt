package io.github.gianpamx.splitbetter.domain.entity

data class Money(val cents: Int = 0) {
    constructor(inMayorCurrencyUnit: Float) : this(inMayorCurrencyUnit.takeUnless { it == 0f }?.times(100)?.toInt() ?: 0)
    constructor(inMayorCurrencyUnit: String) : this(
        try {
            inMayorCurrencyUnit.toFloat()
        } catch (e: Exception) {
            null
        } ?: 0f
    )

    fun inMayorCurrencyUnit() = (this.cents / 100.0).round(2).toFloat()

    operator fun plus(other: Money) = Money(this.cents + other.cents)

    operator fun minus(other: Money) = Money(this.cents - other.cents)

    operator fun compareTo(other: Money) = this.cents - other.cents

    fun toStringInMayorCurrencyUnit(displayZero: Boolean = false) = try {
        takeUnless { cents == 0 && !displayZero }?.inMayorCurrencyUnit()?.toString()
    } catch (_: Throwable) {
        null
    } ?: ""
}

inline fun <T> Iterable<T>.sumOf(selector: (T) -> Money): Money {
    var sum = 0.money
    for (element in this) {
        sum += selector(element)
    }
    return sum
}

operator fun Money.div(i: Int) = Money(this.cents / i)

operator fun Money?.plus(other: Money) = (this ?: 0.money) + other

operator fun Money?.minus(other: Money?) = (this ?: 0.money) - (other ?: 0.money)

operator fun Money.compareTo(other: Int) = this.compareTo(Money(other))

val Int.money: Money get() = Money(this)

fun Money.abs() = Money(kotlin.math.abs(cents))
