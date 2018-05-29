package io.github.gianpamx.splitter.groupexpenses

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.github.gianpamx.splitter.core.Expense
import io.github.gianpamx.splitter.core.ObserveExpensesUseCase
import io.github.gianpamx.splitter.core.SaveExpenseUseCase
import io.github.gianpamx.splitter.core.toAmount
import javax.inject.Inject

class GroupExpensesViewModel @Inject constructor(
        private val saveExpenseUseCase: SaveExpenseUseCase,
        observeExpensesUseCase: ObserveExpensesUseCase
) : ViewModel() {
    val expenses = MutableLiveData<List<ExpenseItem>>()

    init {
        observeExpensesUseCase.invoke {
            expenses.postValue(it.map { it.toExpenseItem() })
        }
    }

    fun createExpense() = saveExpenseUseCase.invoke(Expense()).id
}

private fun Pair<Expense, Int>.toExpenseItem() = ExpenseItem(
        id = first.id,
        title = first.title,
        total = second.toAmount()
)
