package io.github.gianpamx.splitter.core

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import io.github.gianpamx.splitter.core.entity.Expense
import io.github.gianpamx.splitter.core.entity.Payment
import io.github.gianpamx.splitter.core.entity.Person
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

private const val ANY_EXPENSE_ID = 1L

@RunWith(MockitoJUnitRunner::class)
class KeepOrDeleteExpenseTest {
  @Mock private lateinit var persistenceGateway: PersistenceGateway

  private lateinit var testObserver: TestObserver<Any>

  lateinit var keepOrDeleteExpense: KeepOrDeleteExpense

  @Before fun setUp() {
    testObserver = TestObserver()
    whenever(persistenceGateway.findExpenseMaybe(any())).thenReturn(Maybe.just(Expense()))

    keepOrDeleteExpense = KeepOrDeleteExpense(persistenceGateway)

  }

  @Test fun `keep expense`() {
    whenever(persistenceGateway.findReceiversSingle(any())).thenReturn(Single.just(listOf(Person())))
    whenever(persistenceGateway.findPaymentsObservable(any())).thenReturn(Observable.just(listOf(Payment())))

    keepOrDeleteExpense(ANY_EXPENSE_ID).subscribe(testObserver)

    testObserver.assertComplete()
  }

  @Test
  fun `delete expense`() {
    whenever(persistenceGateway.findReceiversSingle(any())).thenReturn(Single.just(emptyList()))
    whenever(persistenceGateway.findPaymentsObservable(any())).thenReturn(Observable.just(emptyList()))
    whenever(persistenceGateway.deleteExpenseCompletable(any())).thenReturn(Completable.complete())

    keepOrDeleteExpense(ANY_EXPENSE_ID).subscribe(testObserver)

    testObserver.assertComplete()
  }
}
