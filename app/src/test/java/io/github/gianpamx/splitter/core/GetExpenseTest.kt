package io.github.gianpamx.splitter.core

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import io.github.gianpamx.splitter.core.entity.Expense
import io.reactivex.Maybe
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
    val testObserver = TestObserver<Expense>()
    whenever(persistenceGateway.findExpenseObservable(any())).thenReturn(Maybe.just(Expense()))

    getExpense.invoke(ANY_EXPENSE_ID).subscribe(testObserver)

    testObserver.assertValue(Expense())
  }
}
