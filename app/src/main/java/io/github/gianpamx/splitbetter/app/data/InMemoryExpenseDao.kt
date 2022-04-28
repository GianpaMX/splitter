package io.github.gianpamx.splitbetter.app.data

import io.github.gianpamx.splitbetter.domain.dao.ExpenseDao
import io.github.gianpamx.splitbetter.domain.entity.Expense
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged

class InMemoryExpenseDao(initialValue: List<Expense>) : ExpenseDao {

    private var expenses: List<Expense>

    private val expensesFlow: MutableSharedFlow<List<Expense>> = MutableSharedFlow(replay = 1)

    override fun findAll(): Flow<List<Expense>> = expensesFlow.distinctUntilChanged()

    init {
        expenses = initialValue
        expensesFlow.tryEmit(expenses)
    }

    override suspend fun save(newExpense: Expense) {
        val alreadyExists = expenses.find { it.id == newExpense.id } != null

        expenses = if (alreadyExists) {
            expenses.map { existingExpense ->
                if (existingExpense.id == newExpense.id) newExpense else existingExpense
            }
        } else {
            expenses + newExpense
        }

        expensesFlow.emit(expenses)
    }

    override suspend fun find(expenseId: String) = expenses.find { it.id == expenseId }
}
