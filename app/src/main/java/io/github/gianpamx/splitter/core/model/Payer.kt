package io.github.gianpamx.splitter.core.model

data class Payer(
        var personId: Long = 0,
        var name: String = "",
        var cents: Int = 0
)
