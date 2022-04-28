package io.github.gianpamx.splitbetter.domain.entity

data class Expense(
    val id: String = "",
    val title: String = "",
    val payers: List<Payer> = emptyList(),
    val receivers: List<Receiver> = emptyList(),
) {

    val total = payers.sumOf { it.amount }
}

fun List<Expense>.total() = sumOf { it.total }
