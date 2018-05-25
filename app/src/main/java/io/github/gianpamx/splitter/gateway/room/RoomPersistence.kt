package io.github.gianpamx.splitter.gateway.room

import io.github.gianpamx.splitter.core.Expense
import io.github.gianpamx.splitter.core.Payment
import io.github.gianpamx.splitter.core.PersistenceGateway
import io.github.gianpamx.splitter.core.Person
import io.github.gianpamx.splitter.gateway.room.model.ExpenseDBModel
import io.github.gianpamx.splitter.gateway.room.model.PaymentDBModel
import io.github.gianpamx.splitter.gateway.room.model.PersonDBModel

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

    override fun observePayments(expenseId: Long, observer: (List<Payment>) -> Unit) {
        databaseDao.observePayments(expenseId).subscribe {
            observer.invoke(it.map {
                it.toPayment(databaseDao.findPerson(it.personId).toPerson())
            })
        }
    }


    override fun createExpense(title: String, description: String): Expense {
        val expense = Expense(title = title, description = description)
        expense.id = databaseDao.insert(expense.toExpenseDBModel())
        return expense
    }
}

private fun Expense.toExpenseDBModel() = ExpenseDBModel(
        id = id,
        title = title,
        description = description
)

private fun ExpenseDBModel.toExpense() = Expense(
        id = id,
        title = title,
        description = description
)

private fun PersonDBModel.toPerson() = Person(
        id = id,
        name = name
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
