package io.github.gianpamx.splitter.core

import io.github.gianpamx.splitter.core.entity.Expense
import io.github.gianpamx.splitter.core.entity.Payer
import io.github.gianpamx.splitter.core.entity.Payment
import io.github.gianpamx.splitter.core.entity.Person
import io.reactivex.Flowable
import io.reactivex.Single

interface PersistenceGateway {
    fun updatePerson(person: Person)
    fun createPerson(person: Person): Long
    fun findPersons(): List<Person>

    fun findPayment(person: Person, expenseId: Long): Payment?
    fun findPayments(expenseId: Long): List<Payment>
    fun observePayments(expenseId: Long): Single<List<Payment>>
    fun createPayment(payment: Payment): Payment
    fun updatePayment(payment: Payment): Payment
    fun deletePayment(expenseId: Long, personId: Long): Payment?
    fun findPaymentsByPersonId(personId: Long): List<Payment>

    fun observePayments(expenseId: Long, observer: (List<Payer>) -> Unit)

    fun observeExpenses(observer: (List<Expense>) -> Unit)
    fun observeExpenses(): Flowable<List<Expense>>
    fun createExpense(expense: Expense): Long
    fun updateExpense(expense: Expense)
    fun findExpense(expenseId: Long): Expense?
    fun findExpensesByPersonId(personId: Long): List<Expense>
    fun deleteExpense(expenseId: Long)

    fun observeReceivers(expenseId: Long, observer: (List<Pair<Person, Boolean>>) -> Unit)
    fun findReceiver(personId: Long, expenseId: Long): Pair<Long, Long>?
    fun findReceivers(expenseId: Long): List<Person>
    fun createReceiver(personId: Long, expenseId: Long)
    fun deleteReceiver(personId: Long, expenseId: Long)
}
