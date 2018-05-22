package io.github.gianpamx.splitter.core

interface PersistenceGateway {
    fun create(payer: Payer)
    fun findAllPayers(): List<Payer>
    fun update(any: Payer)
}
