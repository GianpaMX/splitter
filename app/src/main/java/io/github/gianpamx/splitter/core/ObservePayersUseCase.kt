package io.github.gianpamx.splitter.core

interface ObservePayersUseCase {
    fun invoke(expenseId: Long, observer: (List<Payment>) -> Unit)
}

class ObservePayersUseCaseImpl(private val persistenceGateway: PersistenceGateway) : ObservePayersUseCase {
    override fun invoke(expenseId: Long, observer: (List<Payment>) -> Unit) {
        persistenceGateway.observePayments(expenseId, observer)
    }
}
