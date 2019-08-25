package io.github.gianpamx.splitter.groupexpenses

import io.github.gianpamx.splitter.groupexpenses.model.ExpenseItem

sealed class ExpensesViewState {
    class Ready(val expenses: List<ExpenseItem>, val total: Double) : ExpensesViewState()
    class NewExpense(val id: Long) : ExpensesViewState()
}
