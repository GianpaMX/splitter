package io.github.gianpamx.splitter.core

import io.github.gianpamx.splitter.core.entity.Expense
import io.github.gianpamx.splitter.core.entity.Payer
import io.github.gianpamx.splitter.core.entity.Person
import io.reactivex.rxkotlin.Observables

class GetExpense(private val persistenceGateway: PersistenceGateway) {
  data class Output(
    val expense: Expense,
    val payers: List<Payer>,
    val receivers: List<Pair<Person, Boolean>>,
    val total: Int
  )

  operator fun invoke(expenseId: Long) =
    Observables.combineLatest(
        persistenceGateway.observeExpense(expenseId),
        persistenceGateway.getExpensePayers(expenseId),
        persistenceGateway.getExpenseReceivers(expenseId)
    ) { expense, payers, receivers ->
      Output(expense, payers, receivers, payers.sumBy { it.cents })
    }
}
