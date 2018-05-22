package io.github.gianpamx.splitter.core

interface PersistenceGateway {
    fun create(payer: Payer)
    fun findAllPayers(): List<Payer>
    fun update(payer: Payer)
    fun observePayers(observer: (List<Payer>) -> Unit)
}
