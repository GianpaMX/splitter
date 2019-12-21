package io.github.gianpamx.splitter.core

import io.github.gianpamx.splitter.core.entity.Payer

class GetPayers(private val persistenceGateway: PersistenceGateway) {
  data class Output(val payers: List<Payer>, val total: Int)

  operator fun invoke(expenseId: Long) = persistenceGateway
      .getExpensePayers(expenseId)
      .map { payers ->
        Output(payers, payers.sumBy { it.cents })
      }
}
