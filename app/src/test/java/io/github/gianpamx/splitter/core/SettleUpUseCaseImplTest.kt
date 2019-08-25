package io.github.gianpamx.splitter.core

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.whenever
import io.github.gianpamx.splitter.core.entity.Expense
import io.github.gianpamx.splitter.core.entity.Payment
import io.github.gianpamx.splitter.core.entity.Person
import org.hamcrest.core.IsEqual
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SettleUpUseCaseImplTest {
    @Mock
    private lateinit var persistenceGateway: PersistenceGateway

    private lateinit var settleUpUseCaseImpl: SettleUpUseCaseImpl


    @Before
    fun setUp() {
        settleUpUseCaseImpl = SettleUpUseCaseImpl(persistenceGateway)
    }

    @Test
    fun emptyGroup() {
        val cards = settleUpUseCaseImpl.invoke()

        assertThat(cards, IsEqual(emptyList()))
    }

    @Test
    fun oneExpenseOnePerson() {
        val person = Person(id = 1, name = "P1")
        whenever(persistenceGateway.findPersons()).thenReturn(listOf(person))
        whenever(persistenceGateway.findPaymentsByPersonId(any())).thenReturn(listOf(Payment(expenseId = 1, person = person, cents = 10)))
        whenever(persistenceGateway.findExpensesByPersonId(any())).thenReturn(listOf(Expense(id = 1)))
        whenever(persistenceGateway.findPayments(expenseId = any())).thenReturn(listOf(Payment(expenseId = 1, person = person, cents = 10)))
        whenever(persistenceGateway.findReceivers(any())).thenReturn(listOf(person))


        val cards = settleUpUseCaseImpl.invoke()

        assertThat(cards.size, IsEqual(1))
        assertThat(cards[0].breakdownRows, IsEqual(mutableListOf()))
    }

    @Test
    fun oneExpenseTwoPersonEvenly() {
        val person1 = Person(id = 1, name = "P1")
        val person2 = Person(id = 2, name = "P2")
        whenever(persistenceGateway.findPersons()).thenReturn(listOf(person1, person2))
        whenever(persistenceGateway.findPaymentsByPersonId(eq(1))).thenReturn(listOf(Payment(expenseId = 1, person = person1, cents = 10)))
        whenever(persistenceGateway.findPaymentsByPersonId(eq(2))).thenReturn(emptyList())
        whenever(persistenceGateway.findExpensesByPersonId(any())).thenReturn(listOf(Expense(id = 1)))
        whenever(persistenceGateway.findPayments(expenseId = any())).thenReturn(listOf(Payment(expenseId = 1, person = person1, cents = 10)))
        whenever(persistenceGateway.findReceivers(any())).thenReturn(listOf(person1, person2))


        val cards = settleUpUseCaseImpl.invoke()

        assertThat(cards.size, IsEqual(2))
        assertThat(cards[0].breakdownRows, IsEqual(mutableListOf(Pair(5, "P2"))))
        assertThat(cards[1].breakdownRows, IsEqual(mutableListOf(Pair(-5, "P1"))))
    }

    @Test
    fun oneExpenseOnePersonDidntPay() {
        val person1 = Person(id = 1, name = "P1")
        val person2 = Person(id = 2, name = "P2")
        val person3 = Person(id = 3, name = "P3")
        whenever(persistenceGateway.findPersons()).thenReturn(listOf(person1, person2, person3))
        whenever(persistenceGateway.findPaymentsByPersonId(eq(1))).thenReturn(listOf(Payment(expenseId = 1, person = person1, cents = 10)))
        whenever(persistenceGateway.findPaymentsByPersonId(eq(2))).thenReturn(listOf(Payment(expenseId = 1, person = person2, cents = 20)))
        whenever(persistenceGateway.findPaymentsByPersonId(eq(3))).thenReturn(emptyList())
        whenever(persistenceGateway.findExpensesByPersonId(eq(2))).thenReturn(listOf(Expense(id = 1)))
        whenever(persistenceGateway.findExpensesByPersonId(eq(3))).thenReturn(listOf(Expense(id = 1)))
        whenever(persistenceGateway.findPayments(expenseId = any())).thenReturn(listOf(
                Payment(expenseId = 1, person = person1, cents = 10),
                Payment(expenseId = 1, person = person2, cents = 20))
        )
        whenever(persistenceGateway.findReceivers(any())).thenReturn(listOf(person2, person3))


        val cards = settleUpUseCaseImpl.invoke()

        assertThat(cards.size, IsEqual(3))
        assertThat(cards[0].breakdownRows, IsEqual(mutableListOf(Pair(10, "P3"))))
        assertThat(cards[1].breakdownRows, IsEqual(mutableListOf(Pair(5, "P3"))))
        assertThat(cards[2].breakdownRows, IsEqual(mutableListOf(Pair(-10, "P1"), Pair(-5, "P2"))))
    }
}
