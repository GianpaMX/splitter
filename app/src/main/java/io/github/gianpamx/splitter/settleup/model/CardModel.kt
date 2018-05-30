package io.github.gianpamx.splitter.settleup.model

data class CardModel(
        var personId: Long = 0,
        val personName: String = "",
        var paid: Double = 0.0,
        var spent: Double = 0.0,
        var breakdownRows: List<Pair<Double, String>> = emptyList()
)
