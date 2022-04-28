package io.github.gianpamx.splitbetter.edit

import io.github.gianpamx.splitbetter.domain.entity.Expense
import io.github.gianpamx.splitbetter.domain.entity.Payer
import io.github.gianpamx.splitbetter.domain.entity.Receiver
import io.github.gianpamx.splitbetter.domain.entity.sumOf

data class EditState(
    val expense: Expense? = null,
    val originalExpense: Expense? = null,
    val payers: List<Payer> = emptyList(),
    val receivers: List<Receiver> = emptyList(),
    val selectedPayer: Payer? = null,
    val selectedReceiver: Receiver? = null,
) {
    fun total() = payers.sumOf { it.amount }
}
