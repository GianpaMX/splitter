package io.github.gianpamx.splitter.core

interface ObserveExpensesUseCase {
    fun invoke(observer: (List<Pair<Expense, Int>>, Int) -> Unit)
}

class ObserveExpensesUseCaseImpl(private val persistenceGateway: PersistenceGateway) : ObserveExpensesUseCase {
    override fun invoke(observer: (List<Pair<Expense, Int>>, Int) -> Unit) {
        persistenceGateway.observeExpenses {
            val expenses = it.map {
                Pair(it, persistenceGateway.findPayments(it.id).sumBy { it.cents })
            }
            observer.invoke(expenses, expenses.sumBy { it.second })
        }
    }
}
