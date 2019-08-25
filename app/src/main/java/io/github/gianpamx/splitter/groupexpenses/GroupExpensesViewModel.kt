package io.github.gianpamx.splitter.groupexpenses

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.gianpamx.splitter.core.ObserveExpensesUseCase
import io.github.gianpamx.splitter.core.SaveExpenseUseCase
import io.github.gianpamx.splitter.core.entity.Expense
import io.github.gianpamx.splitter.core.toAmount
import io.github.gianpamx.splitter.groupexpenses.model.ExpenseItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GroupExpensesViewModel @Inject constructor(
        private val saveExpenseUseCase: SaveExpenseUseCase,
        observeExpensesUseCase: ObserveExpensesUseCase
) : ViewModel() {
    val viewState = MutableLiveData<ExpensesViewState>()

    init {
        observeExpensesUseCase.invoke { expenses, total ->
            viewState.postValue(ExpensesViewState.Ready(expenses.map { it.toExpenseItem() }, total.toAmount()))
        }
    }

    fun createExpense() {
        viewModelScope.launch {
            val expense = withContext(Dispatchers.IO) {
                saveExpenseUseCase.invoke(Expense())
            }

            viewState.value = ExpensesViewState.NewExpense(expense.id)
        }
    }
}

private fun Pair<Expense, Int>.toExpenseItem() = ExpenseItem(
        id = first.id,
        title = first.title,
        total = second.toAmount()
)
