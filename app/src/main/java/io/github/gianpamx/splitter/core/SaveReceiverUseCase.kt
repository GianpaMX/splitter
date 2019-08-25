package io.github.gianpamx.splitter.core

import io.github.gianpamx.splitter.core.entity.Person

interface SaveReceiverUseCase {
    fun invoke(isChecked: Boolean, person: Person, expenseId: Long)

}

class SaveReceiverUseCaseImpl(private val persistenceGateway: PersistenceGateway) : SaveReceiverUseCase {
    override fun invoke(isChecked: Boolean, person: Person, expenseId: Long) {
        if (person.id > 0L) {
            persistenceGateway.updatePerson(person)
        } else {
            person.id = persistenceGateway.createPerson(person)
        }

        val receiver = persistenceGateway.findReceiver(person.id, expenseId)

        if (receiver == null && isChecked) {
            persistenceGateway.createReceiver(person.id, expenseId)
        } else if (receiver != null && !isChecked) {
            persistenceGateway.deleteReceiver(person.id, expenseId)
        }
    }
}
