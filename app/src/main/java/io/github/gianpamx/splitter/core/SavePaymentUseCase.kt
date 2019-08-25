package io.github.gianpamx.splitter.core

import io.github.gianpamx.splitter.core.entity.Payment
import io.github.gianpamx.splitter.core.entity.Person

interface SavePaymentUseCase {
    @Throws(Exception::class)
    fun invoke(cents: Int, person: Person, expenseId: Long): Payment?
}

class SavePaymentUseCaseImpl(private val persistenceGateway: PersistenceGateway) : SavePaymentUseCase {
    override fun invoke(cents: Int, person: Person, expenseId: Long): Payment? {
        if (person.id > 0L) {
            persistenceGateway.updatePerson(person)
        } else {
            person.id = persistenceGateway.createPerson(person)
        }

        val payment = Payment(expenseId, person, cents)
        val existingPayment = persistenceGateway.findPayment(person, expenseId)

        return if (cents > 0 && existingPayment == null) {
            persistenceGateway.createPayment(payment)
        } else if (cents > 0) {
            persistenceGateway.updatePayment(payment)
        } else {
            persistenceGateway.deletePayment(expenseId, person.id)
        }
    }
}
