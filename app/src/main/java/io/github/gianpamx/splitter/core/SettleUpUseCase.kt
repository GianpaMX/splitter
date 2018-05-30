package io.github.gianpamx.splitter.core

import kotlin.math.absoluteValue

interface SettleUpUseCase {
    fun invoke(): List<Card>
}

class SettleUpUseCaseImpl(private val persistenceGateway: PersistenceGateway) : SettleUpUseCase {
    override fun invoke(): List<Card> {
        val persons = persistenceGateway.findPersons().map { it.id to it }.toMap()
        val cards = persons.map { (personId, person) ->
            val paid = persistenceGateway.findPaymentsByPersonId(personId).sumBy { it.cents }
            val spent = persistenceGateway.findExpensesByPersonId(personId).map { expense ->
                val expenseTotal = persistenceGateway.findPayments(expense.id).sumBy { it.cents }
                val receivers = persistenceGateway.findReceivers(expense.id)
                expenseTotal / receivers.size
            }.sumBy { it }

            Card(personId = person.id, personName = person.name, centsPaid = paid, centsSpent = spent, breakdownRows = mutableListOf())
        }.map { it.personId to it }.toMap()


        val positiveBalance = cards.values
                .map { it.personId to it.centsPaid - it.centsSpent }
                .filter { it.second > 0 }
                .sortedByDescending { it.second }
                .toMutableList()


        val negativeBalance = cards.values
                .map { it.personId to it.centsPaid - it.centsSpent }
                .filter { it.second < 0 }
                .sortedBy { it.second }
                .map { Pair(it.first, it.second.absoluteValue) }
                .toMutableList()


        for (p in positiveBalance.indices) {
            for (n in negativeBalance.indices) {
                if (positiveBalance[p].second > 0 && negativeBalance[n].second > positiveBalance[p].second) {
                    cards[positiveBalance[p].first]
                            ?.breakdownRows
                            ?.add(Pair(positiveBalance[p].second, persons[negativeBalance[n].first]?.name
                                    ?: ""))
                    cards[negativeBalance[n].first]
                            ?.breakdownRows
                            ?.add(Pair(-positiveBalance[p].second, persons[positiveBalance[p].first]?.name
                                    ?: ""))

                    negativeBalance[n] = negativeBalance[n].copy(second = negativeBalance[n].second - positiveBalance[p].second)
                    positiveBalance[p] = positiveBalance[p].copy(second = 0)
                    break
                } else if (negativeBalance[n].second > 0) {
                    cards[positiveBalance[p].first]
                            ?.breakdownRows
                            ?.add(Pair(negativeBalance[n].second, persons[negativeBalance[n].first]?.name
                                    ?: ""))
                    cards[negativeBalance[n].first]
                            ?.breakdownRows
                            ?.add(Pair(-negativeBalance[n].second, persons[positiveBalance[p].first]?.name
                                    ?: ""))

                    positiveBalance[p] = positiveBalance[p].copy(second = positiveBalance[p].second - negativeBalance[n].second)
                    negativeBalance[n] = negativeBalance[n].copy(second = 0)
                }
            }
        }


        return cards.values.toList()
    }
}
