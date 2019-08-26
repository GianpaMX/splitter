package io.github.gianpamx.splitter.core

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import io.github.gianpamx.splitter.core.entity.Expense
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.subscribers.TestSubscriber
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SaveExpenseTest {
    @Mock
    private lateinit var persistenceGateway: PersistenceGateway

    lateinit var testSubscriber: TestSubscriber<Expense>

    private lateinit var createExpense: SaveExpense

    @Before
    fun setUp() {
        testSubscriber = TestSubscriber()
        createExpense = SaveExpense(persistenceGateway)
    }

    @Test
    fun `New Expense when id is 0`() {
        val newExpense = Expense(id = 0)
        val newId = 1L
        whenever(persistenceGateway.createExpenseSingle(any())).thenReturn(Single.just(newId))

        createExpense(newExpense).toFlowable().subscribe(testSubscriber)

        testSubscriber.assertValue(Expense(newId))
    }

    @Test
    fun `Update Expense when id isn't 0`() {
        val expectedExpense = Expense(id = 27)
        whenever(persistenceGateway.updateExpenseCompletable(any())).thenReturn(Completable.complete())

        createExpense(expectedExpense).toFlowable().subscribe(testSubscriber)

        testSubscriber.assertValue(expectedExpense)
    }
}
