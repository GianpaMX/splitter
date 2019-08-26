package io.github.gianpamx.splitter.frameworks.room

import io.github.gianpamx.splitter.core.PersistenceGateway
import io.github.gianpamx.splitter.core.entity.Expense
import io.github.gianpamx.splitter.core.entity.Payer
import io.github.gianpamx.splitter.core.entity.Payment
import io.github.gianpamx.splitter.core.entity.Person
import io.github.gianpamx.splitter.frameworks.room.model.ExpenseDBModel
import io.github.gianpamx.splitter.frameworks.room.model.PaymentDBModel
import io.github.gianpamx.splitter.frameworks.room.model.PersonDBModel
import io.github.gianpamx.splitter.frameworks.room.model.ReceiverDBModel
import io.github.gianpamx.splitter.frameworks.room.view.PayerDBView
import io.github.gianpamx.splitter.frameworks.room.view.ReceiverDBView
import io.reactivex.Single

class RoomPersistence(private val databaseDao: DatabaseDao) : PersistenceGateway {
    override fun updatePerson(person: Person) {
        databaseDao.update(person.toPersonDBModel())
    }

    override fun createPerson(person: Person) = databaseDao.insert(person.toPersonDBModel())

    override fun findPersons() = databaseDao.findPersons().map { it.toPerson() }


    override fun findPayment(person: Person, expenseId: Long) =
            databaseDao.findPayment(person.id, expenseId)?.toPayment(person)

    override fun findPayments(expenseId: Long) =
            databaseDao.findPayments(expenseId).map {
                it.toPayment(databaseDao.findPerson(it.personId).toPerson())
            }

    override fun observePayments(expenseId: Long) =
            Single.just(findPayments(expenseId))

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

    override fun findPaymentsByPersonId(personId: Long) =
            databaseDao.findPaymentsByPersonId(personId).map {
                it.toPayment(databaseDao.findPerson(it.personId).toPerson())
            }

    override fun observePayments(expenseId: Long, observer: (List<Payer>) -> Unit) {
        databaseDao.observePayments(expenseId).subscribe {
            observer.invoke(it.map {
                it.toPayer()
            })
        }
    }


    override fun observeExpenses(observer: (List<Expense>) -> Unit) {
        databaseDao.observeExpenses().subscribe {
            observer.invoke(it.map { it.toExpense() })
        }
    }

    override fun observeExpenses() = databaseDao.observeExpenses()
            .map { dbExpenses -> dbExpenses.map { it.toExpense() } }

    override fun findExpense(expenseId: Long) =
            databaseDao.findExpense(expenseId)?.toExpense()

    override fun findExpensesByPersonId(personId: Long) = databaseDao.findExpensesByPersonId(personId).map { it.toExpense() }

    override fun createExpense(expense: Expense) =
            databaseDao.insert(expense.toExpenseDBModel())

    override fun createExpenseSingle(expense: Expense) =
            databaseDao.insertSingle(expense.toExpenseDBModel())

    override fun updateExpense(expense: Expense) {
        databaseDao.update(expense.toExpenseDBModel())
    }

    override fun updateExpenseCompletable(expense: Expense) =
            databaseDao.updateCompletable(expense.toExpenseDBModel())

    override fun deleteExpense(expenseId: Long) {
        databaseDao.findExpense(expenseId)?.let {
            databaseDao.deleteExpense(it)
        }
    }


    override fun observeReceivers(expenseId: Long, observer: (List<Pair<Person, Boolean>>) -> Unit) {
        databaseDao.observeReceivers(expenseId).subscribe {
            observer.invoke(it.map {
                Pair(it.toPerson(), it.checked == TRUE)
            })
        }
    }

    override fun findReceiver(personId: Long, expenseId: Long) =
            databaseDao.findReceiver(personId, expenseId)?.toPair()

    override fun findReceivers(expenseId: Long) =
            databaseDao.findReceivers(expenseId).map {
                databaseDao.findPerson(it.personId).toPerson()
            }

    override fun createReceiver(personId: Long, expenseId: Long) {
        databaseDao.insert(ReceiverDBModel(expenseId = expenseId, personId = personId))
    }

    override fun deleteReceiver(personId: Long, expenseId: Long) {
        databaseDao.deleteReceiver(ReceiverDBModel(expenseId = expenseId, personId = personId))
    }
}


private fun ExpenseDBModel.toExpense() = Expense(
        id = id,
        title = title,
        description = description
)

private fun PersonDBModel.toPerson() = Person(
        id = id,
        name = name
)

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
