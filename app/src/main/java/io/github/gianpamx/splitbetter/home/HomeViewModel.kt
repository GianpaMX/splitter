package io.github.gianpamx.splitbetter.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.gianpamx.splitbetter.app.AppAction
import io.github.gianpamx.splitbetter.app.AppActionFlow
import io.github.gianpamx.splitbetter.app.MutableAppActionFlow
import io.github.gianpamx.splitbetter.domain.entity.Expense
import io.github.gianpamx.splitbetter.domain.entity.total
import io.github.gianpamx.splitbetter.domain.usecase.GetExpenses
import io.github.gianpamx.splitbetter.domain.usecase.SettleUp
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    getExpenses: GetExpenses,
    settleUp: SettleUp,
    private val appActionFlow: MutableAppActionFlow,
) : ViewModel() {
    private val homeStateFlow = MutableStateFlow(HomeState())

    private var homeState = homeStateFlow.value
        set(value) {
            field = value
            homeStateFlow.value = value
        }
    val state: StateFlow<HomeState> = homeStateFlow
    val actions: AppActionFlow = appActionFlow

    init {
        settleUp()
            .onEach {
                homeState = homeState.copy(balance = it)
            }
            .launchIn(viewModelScope)

        getExpenses()
            .onEach {
                homeState = homeState.copy(total = it.total())
            }
            .launchIn(viewModelScope)
    }

    fun addExpense() {
        viewModelScope.launch {
            val newExpense = Expense()
            appActionFlow.emit(AppAction.OpenExpenseEditor(newExpense))
        }
    }
}
