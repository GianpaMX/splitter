package io.github.gianpamx.splitter.core

interface KeepOrDeleteExpenseUseCase {
    fun invoke(expenseId: Long)
}

class KeepOrDeleteExpenseUseCaseImpl(private val persistenceGateway: PersistenceGateway) : KeepOrDeleteExpenseUseCase {
    override fun invoke(expenseId: Long) {
        val expense = persistenceGateway.findExpense(expenseId)
        val receivers = persistenceGateway.findReceivers(expenseId)
        val payments = persistenceGateway.findPayments(expenseId)

        if (expense?.title?.isEmpty() == true && receivers.isEmpty() && payments.filter { it.cents > 0 }.isEmpty()) {
            persistenceGateway.deleteExpense(expenseId)
        }
    }
}
