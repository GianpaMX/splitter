package io.github.gianpamx.splitbetter.app

import io.github.gianpamx.splitbetter.domain.entity.Expense
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

sealed class AppAction {
    data class OpenExpenseEditor(val expense: Expense) : AppAction()
    object CloseExpenseEditor : AppAction()
    data class DisplayError(val throwable: Throwable) : AppAction()
}

typealias MutableAppActionFlow = MutableSharedFlow<AppAction>

typealias AppActionFlow = SharedFlow<@JvmSuppressWildcards AppAction>
