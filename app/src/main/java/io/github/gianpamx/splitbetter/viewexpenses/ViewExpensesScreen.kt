package io.github.gianpamx.splitbetter.viewexpenses

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.gianpamx.splitbetter.domain.entity.Expense
import io.github.gianpamx.splitbetter.domain.entity.Money
import io.github.gianpamx.splitbetter.domain.entity.Payer
import io.github.gianpamx.splitbetter.domain.entity.Person

const val containerAnimationInMillis = 500

@Composable
fun ViewExpensesScreen() {
    val viewModel: ViewExpensesViewModel = viewModel()
    val appState: ViewExpensesState by viewModel.state.collectAsState()
    ViewExpensesContent(expenseList = appState.expenses, onExpenseClick = { expense ->
        viewModel.selectExpense(expense)
    })
}

@Composable
fun ViewExpensesContent(expenseList: List<Expense>, onExpenseClick: ((Expense) -> Unit)? = null) {
    ExpenseList(expenseList, onExpenseClick)
}

@Preview
@Composable
fun ViewExpensesContentPreview() {
    ViewExpensesContent(
        expenseList = listOf(
            Expense(
                id = "1",
                title = "Title",
                payers = listOf(
                    Payer(
                        person = Person(id = "1", name = "First Last"),
                        amount = Money(123456)
                    )
                )
            )
        )
    )
}
