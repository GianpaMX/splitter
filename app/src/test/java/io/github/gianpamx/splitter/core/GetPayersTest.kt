package io.github.gianpamx.splitter.core

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import io.github.gianpamx.splitter.core.GetPayers.Output
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

private const val ANY_EXPENSE_ID = 1L

@RunWith(MockitoJUnitRunner::class)
class GetPayersTest {
  @Mock private lateinit var persistenceGateway: PersistenceGateway

  lateinit var getPayers: GetPayers

  @Test fun `empty list of payers`() {
    whenever(persistenceGateway.getExpensePayers(any())).thenReturn(Observable.just(emptyList()))
    getPayers = GetPayers(persistenceGateway)
    val testObserver = TestObserver<Output>()

    getPayers(ANY_EXPENSE_ID).subscribe(testObserver)

    testObserver.assertValue(GetPayers.Output(emptyList(), 0))
  }
}
