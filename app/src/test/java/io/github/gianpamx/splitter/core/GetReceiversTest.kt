package io.github.gianpamx.splitter.core

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import io.github.gianpamx.splitter.core.entity.Person
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

private const val ANY_EXPENSE_ID = 1L

@RunWith(MockitoJUnitRunner::class)
class GetReceiversTest {
  @Mock private lateinit var persistenceGateway: PersistenceGateway

  lateinit var getReceivers: GetReceivers

  @Test fun `empty list of receivers`() {
    whenever(persistenceGateway.getExpenseReceivers(any())).thenReturn(Observable.just(emptyList()))
    getReceivers = GetReceivers(persistenceGateway)

    val testObserver = TestObserver<List<Pair<Person, Boolean>>>()
    getReceivers(ANY_EXPENSE_ID).subscribe(testObserver)

    testObserver.assertValue(emptyList())
  }
}
