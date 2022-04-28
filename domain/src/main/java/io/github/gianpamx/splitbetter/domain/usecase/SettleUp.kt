package io.github.gianpamx.splitbetter.domain.usecase

import io.github.gianpamx.splitbetter.domain.entity.Balance
import io.github.gianpamx.splitbetter.domain.entity.Expense
import io.github.gianpamx.splitbetter.domain.entity.Money
import io.github.gianpamx.splitbetter.domain.entity.Person
import io.github.gianpamx.splitbetter.domain.entity.abs
import io.github.gianpamx.splitbetter.domain.entity.compareTo
import io.github.gianpamx.splitbetter.domain.entity.div
import io.github.gianpamx.splitbetter.domain.entity.minus
import io.github.gianpamx.splitbetter.domain.entity.money
import io.github.gianpamx.splitbetter.domain.entity.plus
import io.github.gianpamx.splitbetter.domain.entity.sumOf
import io.github.gianpamx.splitbetter.domain.dao.ExpenseDao
import kotlinx.coroutines.flow.combine

class SettleUp(
    private val getPeople: GetPeople,
    private val expenseDao: ExpenseDao,
) {
    operator fun invoke() = combine(
        expenseDao.findAll(),
        getPeople(),
    ) { expenses, people ->
        val paid = expenses.groupPaymentsByPerson()
        val spent = expenses.groupExpensesByPerson()

        val indexedPeople = people.groupBy({ it.id }, { it }).mapValues { it.value.first() }

        val groupBy = people
            .groupBy({ it.id }, {
                val money = paid[it] - spent[it]
                money
            })
        val balances = groupBy
            .mapValues {
                it.value.first()
            }

        val positiveBalance = balances.filter { it.value > 0 }.toMutableMap()
        val negativeBalance = balances.filter { it.value < 0 }.mapValues { it.value.abs() }.toMutableMap()

        val breakdownRows = mutableMapOf<String, MutableList<Balance.Row>>()
        people.forEach {
            breakdownRows[it.id] = mutableListOf()
        }

        for (p in positiveBalance.keys) {
            for (n in negativeBalance.keys) {
                val positive = positiveBalance[p]!!
                val negative = negativeBalance[n]!!
                if (positive > 0 && negative > positive) {
                    val borrower = indexedPeople[n]!!
                    breakdownRows[p]?.add(Balance.Row.Borrower(borrower, positive))

                    val lender = indexedPeople[p]!!
                    breakdownRows[n]?.add(Balance.Row.Lender(lender, positive))

                    negativeBalance[n] = negative - positive
                    positiveBalance[p] = 0.money
                    break
                } else if (negative > 0) {
                    val borrower = indexedPeople[n]!!
                    breakdownRows[p]?.add(Balance.Row.Borrower(borrower, negative))

                    val lender = indexedPeople[p]!!
                    breakdownRows[n]?.add(Balance.Row.Lender(lender, negative))

                    positiveBalance[p] = positive - negative
                    negativeBalance[n] = 0.money
                }
            }
        }

        return@combine people.map {
            Balance(
                it,
                paid = paid[it] ?: Money(0),
                spent = spent[it] ?: Money(0),
                breakdownRows = breakdownRows[it.id].orEmpty()
            )
        }
    }
}

private fun List<Expense>.groupPaymentsByPerson(): Map<Person, Money> = this
    .map { it.payers }
    .flatten()
    .groupBy { it.person }
    .mapValues { it.value.sumOf { it.amount } }

private fun List<Expense>.groupExpensesByPerson(): Map<Person, Money> {
    val spent = mutableMapOf<Person, Money>()
    this.forEach { expense ->
        val receivers = expense.receivers.filter { it.isChecked }
        receivers.forEach {
            spent[it.person] += expense.total / receivers.size
        }
    }
    return spent
}
