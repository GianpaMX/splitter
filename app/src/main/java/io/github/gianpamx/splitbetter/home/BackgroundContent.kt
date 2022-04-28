package io.github.gianpamx.splitbetter.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.gianpamx.splitbetter.domain.entity.Balance
import io.github.gianpamx.splitbetter.domain.entity.Money
import io.github.gianpamx.splitbetter.domain.entity.Person
import io.github.gianpamx.splitbetter.domain.entity.money

@Composable
fun BackgroundContent(state: HomeState, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        item {
            Text(
                text = state.total.toStringInMayorCurrencyUnit(),
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
            )
        }

        items(state.balance.size) { i ->
            val balance = state.balance[i]

            val name = balance.person.name
            val paidAmount = balance.paid.toStringInMayorCurrencyUnit(displayZero = true)
            val spentAmount = balance.spent.toStringInMayorCurrencyUnit(displayZero = true)

            Card(
                shape = RoundedCornerShape(8.dp),
                backgroundColor = MaterialTheme.colors.surface,
                elevation = 8.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp)
            ) {
                Column {
                    Text("$name paid $paidAmount and spent $spentAmount", Modifier.padding(24.dp))
                    balance.breakdownRows.forEach {
                        when (it) {
                            is Balance.Row.Lender -> {
                                val amount = it.amount.toStringInMayorCurrencyUnit()
                                val lenderName = it.person.name
                                Text("Pay $amount to $lenderName", Modifier.padding(24.dp))
                            }
                            is Balance.Row.Borrower -> {
                                val amount = it.amount.toStringInMayorCurrencyUnit()
                                val borrowerName = it.person.name
                                Text("Collect $amount from $borrowerName", Modifier.padding(24.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun BackgroundContentPreview() {
    BackgroundContent(
        state = HomeState(
            total = 5_79.money,
            balance = listOf(
                Balance(
                    person = Person("1", "Person 1"),
                    spent = Money(123),
                    paid = Money(0),
                    breakdownRows = listOf(
                        Balance.Row.Lender(Person("2", "Person 2"), 333.money)
                    )
                )
            )
        )
    )
}
