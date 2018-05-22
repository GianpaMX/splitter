package io.github.gianpamx.splitter.core

interface SavePayerUseCase {
    @Throws(Exception::class)
    fun invoke(payer: Payer): List<Payer>
}

class SavePayerUseCaseImpl(private val persistenceGateway: PersistenceGateway) : SavePayerUseCase {
    override fun invoke(payer: Payer): List<Payer> {
        if (payer.id > 0L) {
            persistenceGateway.update(payer)
        } else {
            persistenceGateway.create(payer)
        }

        return persistenceGateway.findAllPayers()
    }
}
