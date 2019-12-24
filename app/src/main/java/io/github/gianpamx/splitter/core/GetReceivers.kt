package io.github.gianpamx.splitter.core

class GetReceivers(private val persistenceGateway: PersistenceGateway) {
  operator fun invoke(expenseId: Long) =
    persistenceGateway.getExpenseReceivers(expenseId)
}
