package io.github.gianpamx.splitter.core.model

data class Card(
        val personId: Long = 0,
        val personName: String = "",
        val centsPaid: Int = 0,
        val centsSpent: Int = 0,
        val breakdownRows: MutableList<Pair<Int, String>> = mutableListOf()
)
