package io.github.gianpamx.splitter.core

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import io.github.gianpamx.splitter.core.entity.Payment
import io.github.gianpamx.splitter.core.entity.Person
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

private const val anyExpenseId = 1L
private val anyPerson = Person(id = 1)
private const val anyCents = 100

@RunWith(MockitoJUnitRunner::class)
class SavePaymentTest {
  @Mock private lateinit var persistenceGateway: PersistenceGateway

  private lateinit var testObserver: TestObserver<Payment>

  private val expectedPayment = Payment(anyExpenseId, anyPerson, anyCents)

  private lateinit var savePayment: SavePayment

  @Before fun setUp() {
    testObserver = TestObserver()

    whenever(persistenceGateway.updatePersonObservable(any())).thenReturn(Observable.just(anyPerson))

    savePayment = SavePayment(persistenceGateway)
  }

  @Test fun `create new payment`() {
    whenever(persistenceGateway.findPaymentObservable(any(), any())).thenReturn(Observable.empty())
    whenever(persistenceGateway.createPaymentObservable(any())).thenReturn(Observable.just(expectedPayment))
    whenever(persistenceGateway.updatePaymentObservable(any())).thenReturn(Observable.just(expectedPayment))

    savePayment(anyCents, anyPerson, anyExpenseId).subscribe(testObserver)

    testObserver.assertValue(expectedPayment)
  }

  @Test fun `update existing payment`() {
    whenever(persistenceGateway.findPaymentObservable(any(), any())).thenReturn(Observable.just(expectedPayment))
    whenever(persistenceGateway.updatePaymentObservable(any())).thenReturn(Observable.just(expectedPayment))

    savePayment(anyCents, anyPerson, anyExpenseId).subscribe(testObserver)

    testObserver.assertValue(expectedPayment)
  }

  @Test fun `delete payment`() {
    whenever(persistenceGateway.deletePaymentCompletable(any(), any())).thenReturn(Completable.complete())

    savePayment.invoke(0, anyPerson, anyExpenseId).subscribe(testObserver)

    testObserver
        .assertNoValues()
        .assertComplete()
  }
}
