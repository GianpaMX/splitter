package io.github.gianpamx.splitter.core

import io.github.gianpamx.splitter.core.entity.Expense

interface SaveExpenseUseCase {
    fun invoke(expense: Expense): Expense
}

class SaveExpenseUseCaseImpl(private val persistenceGateway: PersistenceGateway) : SaveExpenseUseCase {
    override fun invoke(expense: Expense): Expense {
        if (expense.id > 0) {
            persistenceGateway.updateExpense(expense)
        } else {
            expense.id = persistenceGateway.createExpense(expense)
        }
        return expense
    }
}
