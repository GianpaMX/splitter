package io.github.gianpamx.splitter.core

class GetExpense(private val persistenceGateway: PersistenceGateway) {
  operator fun invoke(expenseId: Long) = persistenceGateway.findExpenseObservable(expenseId)
}
