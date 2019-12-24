package io.github.gianpamx.splitter.core

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import io.github.gianpamx.splitter.core.entity.Person
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

private const val anyPersonId = 1L
private const val anyExpenseId = 1L
private val anyPerson = Person(id = anyPersonId)

@RunWith(MockitoJUnitRunner::class)
class SaveReceiverTest {
  @Mock
  private lateinit var persistenceGateway: PersistenceGateway

  private lateinit var testObserver: TestObserver<Any>

  private lateinit var saveReceiver: SaveReceiver

  @Before
  fun setUp() {
    whenever(persistenceGateway.updatePersonObservable(any())).thenReturn(Observable.just(anyPerson))

    testObserver = TestObserver()

    saveReceiver = SaveReceiver(persistenceGateway)
  }

  @Test
  fun createPayment() {
    whenever(persistenceGateway.findReceiverObservable(any(), any())).thenReturn(Observable.empty())
    whenever(persistenceGateway.createReceiverCompletable(any(), any())).thenReturn(Completable.complete())

    saveReceiver(true, anyPerson, anyExpenseId)
        .toObservable<Any>()
        .subscribe(testObserver)

    testObserver.assertComplete()
  }

  @Test
  fun deletePayment() {
    whenever(persistenceGateway.findReceiverObservable(any(), any()))
        .thenReturn(Observable.just(Pair(anyPersonId, anyExpenseId)))
    whenever(persistenceGateway.deleteReceiverCompletable(any(), any()))
        .thenReturn(Completable.complete())

    saveReceiver(false, anyPerson, anyExpenseId)
        .toObservable<Any>()
        .subscribe(testObserver)

    testObserver.assertComplete()
  }
}
