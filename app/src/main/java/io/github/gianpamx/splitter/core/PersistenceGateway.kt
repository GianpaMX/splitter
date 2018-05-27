package io.github.gianpamx.splitter.core

interface PersistenceGateway {
    fun updatePerson(person: Person)
    fun createPerson(person: Person): Long

    fun findPayment(person: Person, expenseId: Long): Payment?
    fun createPayment(payment: Payment): Payment
    fun updatePayment(payment: Payment): Payment
    fun deletePayment(expenseId: Long, personId: Long): Payment?

    fun observePayments(expenseId: Long, observer: (List<Payer>) -> Unit)

    fun createExpense(title: String, description: String): Expense

    fun observeReceivers(expenseId: Long, observer: (List<Pair<Person, Boolean>>) -> Unit)
    fun findReceiver(personId: Long, expenseId: Long): Pair<Long, Long>?
    fun createReceiver(personId: Long, expenseId: Long)
    fun deleteReceiver(personId: Long, expenseId: Long)
}
