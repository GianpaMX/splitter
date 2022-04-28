package io.github.gianpamx.splitbetter.viewexpenses

import io.github.gianpamx.splitbetter.domain.entity.Expense

data class ViewExpensesState(
    val expenses: List<Expense> = emptyList(),
)
