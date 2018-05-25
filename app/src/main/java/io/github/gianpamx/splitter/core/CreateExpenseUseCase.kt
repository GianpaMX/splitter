package io.github.gianpamx.splitter.core

interface CreateExpenseUseCase {
    fun invoke(title: String = "", description: String = ""): Expense
}

class CreateExpenseUseCaseImpl(private val persistenceGateway: PersistenceGateway) : CreateExpenseUseCase {
    override fun invoke(title: String, description: String): Expense {
        return persistenceGateway.createExpense(title, description)
    }
}
