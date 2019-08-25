package io.github.gianpamx.splitter.core

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import io.github.gianpamx.splitter.core.entity.Expense
import io.github.gianpamx.splitter.core.entity.Payment
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.subscribers.TestSubscriber
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ObserveExpensesTest {
    @Mock
    lateinit var persistenceGateway: PersistenceGateway

    lateinit var testSubscriber: TestSubscriber<ObserveExpenses.Output>

    lateinit var observeExpenses: ObserveExpenses

    @Before
    fun setUp() {
        testSubscriber = TestSubscriber()
        observeExpenses = ObserveExpenses(persistenceGateway)
    }

    @Test
    fun `No Expenses`() {
        whenever(persistenceGateway.observeExpenses()).thenReturn(Flowable.never())

        observeExpenses().subscribe(testSubscriber)

        testSubscriber.assertValue(ObserveExpenses.Output(emptyList(), 0))
    }

    @Test
    fun `One expense One Payment`() {
        whenever(persistenceGateway.observeExpenses()).thenReturn(Flowable.concat(Flowable.just(listOf(Expense())), Flowable.never()))
        whenever(persistenceGateway.observePayments(any())).thenReturn(Single.just(listOf(Payment(cents = 10))))

        observeExpenses().subscribe(testSubscriber)

        testSubscriber.assertValueAt(1, ObserveExpenses.Output(listOf(ObserveExpenses.TotalExpense(total = 10)), 10))
    }

    @Test
    fun `One expense Two Payments`() {
        whenever(persistenceGateway.observeExpenses()).thenReturn(Flowable.concat(Flowable.just(listOf(Expense())), Flowable.never()))
        whenever(persistenceGateway.observePayments(any())).thenReturn(Single.just(listOf(Payment(cents = 10), Payment(cents = 20))))

        observeExpenses().subscribe(testSubscriber)

        testSubscriber.assertValueAt(1, ObserveExpenses.Output(listOf(ObserveExpenses.TotalExpense(total = 30)), 30))
    }

    @Test
    fun `Two expenses One Payment each`() {
        whenever(persistenceGateway.observeExpenses()).thenReturn(Flowable.concat(Flowable.just(listOf(Expense(1), Expense(2))), Flowable.never()))
        whenever(persistenceGateway.observePayments(1)).thenReturn(Single.just(listOf(Payment(cents = 10))))
        whenever(persistenceGateway.observePayments(2)).thenReturn(Single.just(listOf(Payment(cents = 30))))

        observeExpenses().subscribe(testSubscriber)

        testSubscriber.assertValueAt(1, ObserveExpenses.Output(listOf(ObserveExpenses.TotalExpense(id = 1, total = 10), ObserveExpenses.TotalExpense(id = 2, total = 30)), 40))
    }

    @Test
    fun `Two expenses Two Payments each`() {
        whenever(persistenceGateway.observeExpenses()).thenReturn(Flowable.concat(Flowable.just(listOf(Expense(1), Expense(2))), Flowable.never()))
        whenever(persistenceGateway.observePayments(1)).thenReturn(Single.just(listOf(Payment(cents = 10), Payment(cents = 20))))
        whenever(persistenceGateway.observePayments(2)).thenReturn(Single.just(listOf(Payment(cents = 30), Payment(cents = 40))))

        observeExpenses().subscribe(testSubscriber)

        testSubscriber.assertValueAt(1, ObserveExpenses.Output(listOf(ObserveExpenses.TotalExpense(id = 1, total = 30), ObserveExpenses.TotalExpense(id = 2, total = 70)), 100))
    }
}
