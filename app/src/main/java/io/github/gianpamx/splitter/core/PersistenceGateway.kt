package io.github.gianpamx.splitter.core

import io.github.gianpamx.splitter.core.entity.Expense
import io.github.gianpamx.splitter.core.entity.Payer
import io.github.gianpamx.splitter.core.entity.Payment
import io.github.gianpamx.splitter.core.entity.Person
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

interface PersistenceGateway {
  fun updatePerson(person: Person)
  fun updatePersonObservable(person: Person): Observable<Person>
  fun createPerson(person: Person): Long
  fun createPersonObservable(person: Person): Observable<Person>
  fun findPersons(): List<Person>

  fun findPayment(person: Person, expenseId: Long): Payment?
  fun findPaymentObservable(person: Person, expenseId: Long): Observable<Payment>
  fun findPayments(expenseId: Long): List<Payment>
  fun observePayments(expenseId: Long): Single<List<Payment>>
  fun createPayment(payment: Payment): Payment
  fun createPaymentObservable(payment: Payment): Observable<Payment>
  fun updatePayment(payment: Payment): Payment
  fun updatePaymentObservable(payment: Payment): Observable<Payment>
  fun deletePayment(expenseId: Long, personId: Long): Payment?
  fun deletePaymentObservable(expenseId: Long, personId: Long): Completable
  fun findPaymentsByPersonId(personId: Long): List<Payment>

  fun observePayments(expenseId: Long, observer: (List<Payer>) -> Unit)
  fun getExpensePayers(expenseId: Long): Observable<List<Payer>>

  fun observeExpenses(observer: (List<Expense>) -> Unit)
  fun observeExpenses(): Flowable<List<Expense>>
  @Deprecated("refactor: Rx") fun createExpense(expense: Expense): Long

  fun createExpenseSingle(expense: Expense): Single<Long>
  @Deprecated("refactor: Rx") fun updateExpense(expense: Expense)

  fun updateExpenseCompletable(expense: Expense): Completable
  fun findExpense(expenseId: Long): Expense?
  fun findExpenseObservable(expenseId: Long): Maybe<Expense>
  fun findExpensesByPersonId(personId: Long): List<Expense>
  fun deleteExpense(expenseId: Long)

  fun observeReceivers(expenseId: Long, observer: (List<Pair<Person, Boolean>>) -> Unit)
  fun findReceiver(personId: Long, expenseId: Long): Pair<Long, Long>?
  fun findReceivers(expenseId: Long): List<Person>
  fun createReceiver(personId: Long, expenseId: Long)
  fun deleteReceiver(personId: Long, expenseId: Long)
}
