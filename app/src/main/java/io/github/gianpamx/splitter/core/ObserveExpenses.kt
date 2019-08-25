package io.github.gianpamx.splitter.core

import io.github.gianpamx.splitter.core.entity.Expense
import io.reactivex.Flowable
import io.reactivex.rxkotlin.zipWith

class ObserveExpenses(private val persistenceGateway: PersistenceGateway) {
    data class TotalExpense(val id: Long = 0, val title: String = "", val total: Int = 0)
    data class Output(val expenses: List<TotalExpense> = emptyList(), val total: Int = 0)

    private val emptyOutput = Output()

    operator fun invoke() = persistenceGateway.observeExpenses()
            .flatMap {
                val eachExpense = Flowable.fromIterable(it)
                        .publish()
                        .autoConnect(2)

                val eachTotal = eachExpense
                        .flatMapSingle {
                            persistenceGateway.observePayments(it.id)
                        }
                        .flatMap {
                            Flowable.fromIterable(it)
                                    .map {
                                        it.cents
                                    }
                                    .reduce(0) { t1, t2 ->
                                        t1 + t2
                                    }
                                    .toFlowable()
                        }

                eachExpense.zipWith(eachTotal) { e, t -> e.toTotalExpense(t) }
                        .toList()
                        .toFlowable()
            }
            .map { it.toOutput() }
            .startWith(emptyOutput)

    private fun Expense.toTotalExpense(total: Int) = TotalExpense(
            id = this.id,
            title = this.title,
            total = total
    )

    private fun List<TotalExpense>.toOutput() = Output(this, sumBy { it.total })
}
