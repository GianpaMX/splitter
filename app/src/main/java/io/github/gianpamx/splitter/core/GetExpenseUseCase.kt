package io.github.gianpamx.splitter.core

import io.github.gianpamx.splitter.core.model.Expense

interface GetExpenseUseCase {
    fun invoke(expenseId: Long): Expense
}

class GetExpenseUseCaseImpl(private val persistenceGateway: PersistenceGateway) : GetExpenseUseCase {
    override fun invoke(expenseId: Long) =
            persistenceGateway.findExpense(expenseId) ?: throw Exception("Not found")
}
