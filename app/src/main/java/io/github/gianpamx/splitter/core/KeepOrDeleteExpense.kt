package io.github.gianpamx.splitter.core

import io.reactivex.Completable
import io.reactivex.rxkotlin.Observables

class KeepOrDeleteExpense(private val persistenceGateway: PersistenceGateway) {
  operator fun invoke(expenseId: Long) = Observables.zip(
      persistenceGateway.findExpenseMaybe(expenseId).toObservable(),
      persistenceGateway.findReceiversSingle(expenseId).toObservable(),
      persistenceGateway.findPaymentsObservable(expenseId)
  ) { expense, receivers, payments ->
    expense.title.isEmpty() && receivers.isEmpty() && payments.filter { it.cents > 0 }.isEmpty()
  }.flatMapCompletable {
    if (it) {
      persistenceGateway.deleteExpenseCompletable(expenseId)
    } else {
      Completable.complete()
    }
  }
}
