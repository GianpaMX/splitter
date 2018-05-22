package io.github.gianpamx.splitter.core

interface ObservePayersUseCase {
    fun invoke(observer: (List<Payer>) -> Unit)
}

class ObservePayersUseCaseImpl(private val persistenceGateway: PersistenceGateway) : ObservePayersUseCase {
    override fun invoke(observer: (List<Payer>) -> Unit) {
        persistenceGateway.observePayers(observer)
    }
}
