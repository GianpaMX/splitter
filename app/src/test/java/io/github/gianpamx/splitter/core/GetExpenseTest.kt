package io.github.gianpamx.splitter.core

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import io.github.gianpamx.splitter.core.GetExpense.Output
import io.github.gianpamx.splitter.core.entity.Expense
import io.github.gianpamx.splitter.core.entity.Payer
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

private const val ANY_EXPENSE_ID = 1L

@RunWith(MockitoJUnitRunner::class)
class GetExpenseTest {
  @Mock private lateinit var persistenceGateway: PersistenceGateway

  private lateinit var getExpense: GetExpense

  @Before fun setUp() {
    getExpense = GetExpense(persistenceGateway)
  }

  @Test fun `existing expense`() {
    val testObserver = TestObserver<Output>()
    whenever(persistenceGateway.observeExpense(any())).thenReturn(Observable.just(Expense()))
    whenever(persistenceGateway.getExpensePayers(any())).thenReturn(Observable.just(emptyList()))
    whenever(persistenceGateway.getExpenseReceivers(any())).thenReturn(Observable.just(emptyList()))

    getExpense.invoke(ANY_EXPENSE_ID).subscribe(testObserver)

    testObserver.assertValue(Output(expense = Expense(), payers = emptyList(), receivers = emptyList(), total = 0))
  }

  @Test fun `expense total`() {
    val testObserver = TestObserver<Output>()
    val listOfPayers = listOf(Payer(cents = 10), Payer(cents = 20))
    whenever(persistenceGateway.observeExpense(any())).thenReturn(Observable.just(Expense()))
    whenever(persistenceGateway.getExpensePayers(any())).thenReturn(Observable.just(listOfPayers))
    whenever(persistenceGateway.getExpenseReceivers(any())).thenReturn(Observable.just(emptyList()))

    getExpense.invoke(ANY_EXPENSE_ID).subscribe(testObserver)

    testObserver.assertValue(Output(expense = Expense(), payers = listOfPayers, receivers = emptyList(), total = 30))
  }
}
