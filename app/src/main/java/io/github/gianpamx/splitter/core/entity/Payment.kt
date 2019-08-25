package io.github.gianpamx.splitter.core.entity

data class Payment(
        var expenseId: Long = 0,
        var person: Person = Person(),
        var cents: Int = 0
)
