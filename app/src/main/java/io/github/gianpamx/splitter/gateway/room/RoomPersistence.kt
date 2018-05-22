package io.github.gianpamx.splitter.gateway.room

import io.github.gianpamx.splitter.core.Payer
import io.github.gianpamx.splitter.core.PersistenceGateway

class RoomPersistence(private val databaseDao: DatabaseDao) : PersistenceGateway {
    override fun create(payer: Payer) {
        databaseDao.insert(payer.toPayerDBModel())
    }

    override fun findAllPayers() = databaseDao.retriveAllPayers().map { it.toPayer() }

    override fun update(payer: Payer) {
        databaseDao.update(payer.toPayerDBModel())
    }

    override fun observePayers(observer: (List<Payer>) -> Unit) {
        databaseDao
                .allPayers()
                .subscribe {
                    observer.invoke(it.map { it.toPayer() })
                }
    }
}

private fun PayerDBModel.toPayer() = Payer(
        id = id,
        name = name,
        cents = cents
)

private fun Payer.toPayerDBModel() = PayerDBModel(
        id = id,
        name = name,
        cents = cents
)
