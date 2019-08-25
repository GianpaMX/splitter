package io.github.gianpamx.splitter.core.entity

data class Payer(
        var personId: Long = 0,
        var name: String = "",
        var cents: Int = 0
)
