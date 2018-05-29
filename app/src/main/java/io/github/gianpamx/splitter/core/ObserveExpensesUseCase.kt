package io.github.gianpamx.splitter.core

interface ObserveExpensesUseCase {
    fun invoke(observer: (List<Pair<Expense, Int>>) -> Unit)
}

class ObserveExpensesUseCaseImpl(private val persistenceGateway: PersistenceGateway) : ObserveExpensesUseCase {
    override fun invoke(observer: (List<Pair<Expense, Int>>) -> Unit) {
        persistenceGateway.observeExpenses {
            observer.invoke(it.map {
                Pair(it, persistenceGateway.findPayments(it.id).sumBy { it.cents })
            })
        }
    }
}
