package io.github.gianpamx.splitter.gateway.room

import io.github.gianpamx.splitter.core.*
import io.github.gianpamx.splitter.gateway.room.model.ExpenseDBModel
import io.github.gianpamx.splitter.gateway.room.model.PaymentDBModel
import io.github.gianpamx.splitter.gateway.room.model.PersonDBModel
import io.github.gianpamx.splitter.gateway.room.model.ReceiverDBModel
import io.github.gianpamx.splitter.gateway.room.view.PayerDBView
import io.github.gianpamx.splitter.gateway.room.view.ReceiverDBView

class RoomPersistence(private val databaseDao: DatabaseDao) : PersistenceGateway {
    override fun updatePerson(person: Person) {
        databaseDao.update(person.toPersonDBModel())
    }

    override fun createPerson(person: Person) = databaseDao.insert(person.toPersonDBModel())

    override fun findPayment(person: Person, expenseId: Long) = databaseDao.findPayment(person.id, expenseId)?.toPayment(person)

    override fun createPayment(payment: Payment): Payment {
        databaseDao.insert(payment.toPaymentDBModel())
        return payment
    }


    override fun updatePayment(payment: Payment): Payment {
        databaseDao.update(payment.toPaymentDBModel())
        return payment
    }

    override fun deletePayment(expenseId: Long, personId: Long): Payment? {
        databaseDao.findPayment(expenseId, personId)?.let {
            databaseDao.deletePayment(it)
        }
        return null
    }

    override fun observePayments(expenseId: Long, observer: (List<Payer>) -> Unit) {
        databaseDao.observePayments(expenseId).subscribe {
            observer.invoke(it.map {
                it.toPayer()
            })
        }
    }


    override fun createExpense(title: String, description: String): Expense {
        val expense = Expense(title = title, description = description)
        expense.id = databaseDao.insert(expense.toExpenseDBModel())
        return expense
    }


    override fun observeReceivers(expenseId: Long, observer: (List<Pair<Person, Boolean>>) -> Unit) {
        databaseDao.observeReceivers(expenseId).subscribe {
            observer.invoke(it.map {
                Pair(it.toPerson(), it.checked == TRUE)
            })
        }
    }

    override fun findReceiver(personId: Long, expenseId: Long) = databaseDao.findReceiver(personId, expenseId)?.toPair()

    override fun createReceiver(personId: Long, expenseId: Long) {
        databaseDao.insert(ReceiverDBModel(expenseId = expenseId, personId = personId))
    }

    override fun deleteReceiver(personId: Long, expenseId: Long) {
        databaseDao.deleteReceiver(ReceiverDBModel(expenseId = expenseId, personId = personId))
    }
}

private fun ReceiverDBModel.toPair() = Pair(personId, expenseId)

private fun ReceiverDBView.toPerson() = Person(
        id = personId,
        name = name
)

private fun PayerDBView.toPayer() = Payer(
        personId = personId,
        name = name,
        cents = cents
)

private fun Expense.toExpenseDBModel() = ExpenseDBModel(
        id = id,
        title = title,
        description = description
)

private fun PaymentDBModel.toPayment(person: Person) = Payment(
        expenseId = expenseId,
        person = person,
        cents = cents
)

private fun Payment.toPaymentDBModel() = PaymentDBModel(
        expenseId = expenseId,
        personId = person.id,
        cents = cents
)

private fun Person.toPersonDBModel() = PersonDBModel(
        id = id,
        name = name
)
