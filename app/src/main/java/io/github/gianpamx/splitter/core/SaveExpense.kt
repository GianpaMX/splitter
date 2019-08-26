package io.github.gianpamx.splitter.core

import io.github.gianpamx.splitter.core.entity.Expense
import io.reactivex.Single

class SaveExpense(private val persistenceGateway: PersistenceGateway) {
    operator fun invoke(expense: Expense) = if (expense.id > 0) {
        persistenceGateway
                .updateExpenseCompletable(expense)
                .andThen(Single.just(expense))
    } else {
        persistenceGateway
                .createExpenseSingle(expense)
                .map { newId ->
                    expense.copy(id = newId)
                }
    }
}
