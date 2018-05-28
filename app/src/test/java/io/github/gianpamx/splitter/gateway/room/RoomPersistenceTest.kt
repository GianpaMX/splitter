package io.github.gianpamx.splitter.gateway.room

import com.nhaarman.mockito_kotlin.*
import io.github.gianpamx.splitter.core.Expense
import io.github.gianpamx.splitter.core.Payer
import io.github.gianpamx.splitter.core.Payment
import io.github.gianpamx.splitter.core.Person
import io.github.gianpamx.splitter.gateway.room.model.ExpenseDBModel
import io.github.gianpamx.splitter.gateway.room.model.PaymentDBModel
import io.github.gianpamx.splitter.gateway.room.model.ReceiverDBModel
import io.github.gianpamx.splitter.gateway.room.view.PayerDBView
import io.github.gianpamx.splitter.gateway.room.view.ReceiverDBView
import io.reactivex.internal.operators.flowable.FlowableJust
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

private const val anyExpenseId = 1L
private val anyPerson = Person(id = 1)
private val anyPayment = Payment(cents = 12345, expenseId = anyExpenseId, person = anyPerson)

@RunWith(MockitoJUnitRunner::class)
class RoomPersistenceTest {

    @Mock
    private lateinit var databaseDao: DatabaseDao

    private lateinit var roomPersistence: RoomPersistence

    @Before
    fun setUp() {
        roomPersistence = RoomPersistence(databaseDao)
    }

    @Test
    fun create() {

        roomPersistence.createPayment(anyPayment)

        verify(databaseDao).insert(eq(PaymentDBModel(cents = anyPayment.cents, expenseId = anyPayment.expenseId, personId = anyPerson.id)))
    }

    @Test
    fun update() {
        val expectedPayer = anyPayment

        roomPersistence.updatePayment(expectedPayer)

        verify(databaseDao).update(eq(PaymentDBModel(cents = anyPayment.cents, expenseId = anyPayment.expenseId, personId = anyPerson.id)))
    }

    @Test
    fun observePayers() {
        val observer = mock<(List<Payer>) -> Unit>()
        whenever(databaseDao.observePayments(any())).thenReturn(FlowableJust(listOf(PayerDBView(personId = anyPerson.id, name = anyPerson.name, cents = 12345))))

        roomPersistence.observePayments(anyPayment.expenseId, observer)

        verify(observer).invoke(eq(listOf(Payer(personId = anyPerson.id, name = anyPerson.name, cents = 12345))))
    }

    @Test
    fun observeReceivers() {
        val observer = mock<(List<Pair<Person, Boolean>>) -> Unit>()
        whenever(databaseDao.observeReceivers(any())).thenReturn(FlowableJust(listOf(ReceiverDBView(personId = anyPerson.id, name = anyPerson.name, checked = FALSE))))

        roomPersistence.observeReceivers(anyPayment.expenseId, observer)

        verify(observer).invoke(eq(listOf(Pair(Person(id = anyPerson.id, name = anyPerson.name), false))))
    }

    @Test
    fun deletePayment() {
        whenever(databaseDao.findPayment(any(), any())).thenReturn(PaymentDBModel())

        roomPersistence.deletePayment(anyPayment.expenseId, anyPerson.id)

        verify(databaseDao).deletePayment(any())
    }

    @Test
    fun findReceiver() {
        roomPersistence.findReceiver(anyPerson.id, anyExpenseId)

        verify(databaseDao).findReceiver(any(), any())
    }

    @Test
    fun createReceiver() {
        roomPersistence.createReceiver(anyPerson.id, anyExpenseId)

        verify(databaseDao).insert(any<ReceiverDBModel>())
    }

    @Test
    fun deleteReceiver() {
        roomPersistence.deleteReceiver(anyPerson.id, anyExpenseId)

        verify(databaseDao).deleteReceiver(any())
    }

    @Test
    fun findPayments() {
        roomPersistence.findPayments(anyExpenseId)

        verify(databaseDao).findPayments(any())
    }

    @Test
    fun findExpense() {
        roomPersistence.findExpense(anyExpenseId)

        verify(databaseDao).findExpense(any())
    }

    @Test
    fun deleteExpense() {
        whenever(databaseDao.findExpense(any())).thenReturn(ExpenseDBModel())

        roomPersistence.deleteExpense(anyExpenseId)

        verify(databaseDao).deleteExpense(any())
    }

    @Test
    fun findReceivers() {
        roomPersistence.findReceivers(anyExpenseId)

        verify(databaseDao).findReceivers(any())
    }

    @Test
    fun createExpense() {
        roomPersistence.createExpense(Expense())

        verify(databaseDao).insert(any<ExpenseDBModel>())
    }

    @Test
    fun updateExpense() {
        roomPersistence.updateExpense(Expense())

        verify(databaseDao).update(any<ExpenseDBModel>())
    }
}
