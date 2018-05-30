package io.github.gianpamx.splitter.groupexpenses.model

data class ExpenseItem(
        var id: Long = 0,
        var title: String = "",
        val total: Double = 0.0
)
