package io.github.gianpamx.splitbetter.viewexpenses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.gianpamx.splitbetter.app.AppAction
import io.github.gianpamx.splitbetter.app.MutableAppActionFlow
import io.github.gianpamx.splitbetter.domain.entity.Expense
import io.github.gianpamx.splitbetter.domain.usecase.GetExpenses
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@HiltViewModel
class ViewExpensesViewModel @Inject constructor(
    getExpenses: GetExpenses,
    private val appActionFlow: MutableAppActionFlow,
) : ViewModel() {
    private val viewExpensesStateFlow = MutableStateFlow(ViewExpensesState())
    private var viewExpensesState = viewExpensesStateFlow.value
        set(value) {
            field = value
            viewExpensesStateFlow.value = value
        }
    val state: StateFlow<ViewExpensesState> = viewExpensesStateFlow

    init {
        getExpenses()
            .onEach {
                viewExpensesState = viewExpensesState.copy(
                    expenses = it
                )
            }
            .launchIn(viewModelScope)
    }

    fun selectExpense(expense: Expense) {
        viewModelScope.launch {
            appActionFlow.emit(AppAction.OpenExpenseEditor(expense))
        }
    }
}
