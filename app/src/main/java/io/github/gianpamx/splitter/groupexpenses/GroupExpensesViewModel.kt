package io.github.gianpamx.splitter.groupexpenses

import android.arch.lifecycle.ViewModel
import io.github.gianpamx.splitter.core.Expense
import io.github.gianpamx.splitter.core.SaveExpenseUseCase
import io.github.gianpamx.splitter.expense.ExpenseModel
import javax.inject.Inject

class GroupExpensesViewModel @Inject constructor(private val saveExpenseUseCase: SaveExpenseUseCase) : ViewModel() {
    fun createExpense() = saveExpenseUseCase.invoke(Expense()).toExpenseModel()
}

private fun Expense.toExpenseModel() = ExpenseModel(
        id = id,
        title = title,
        description = description
)
