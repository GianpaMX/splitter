package io.github.gianpamx.splitter.core

interface ObserveReceiversUseCase {
    fun invoke(expenseId: Long, observer: (List<Pair<Person, Boolean>>) -> Unit)
}

class ObserveReceiversUseCaseImpl(private val persistenceGateway: PersistenceGateway) : ObserveReceiversUseCase {
    override fun invoke(expenseId: Long, observer: (List<Pair<Person, Boolean>>) -> Unit) {
        persistenceGateway.observeReceivers(expenseId, observer)
    }
}
