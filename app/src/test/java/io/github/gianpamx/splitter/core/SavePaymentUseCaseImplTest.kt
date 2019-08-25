package io.github.gianpamx.splitter.core

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.github.gianpamx.splitter.core.entity.Payment
import io.github.gianpamx.splitter.core.entity.Person
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

private const val anyExpenseId = 1L
private val anyPerson = Person(id = 1)
private const val anyCents = 100

@RunWith(MockitoJUnitRunner::class)
class SavePaymentUseCaseImplTest {
    @Mock
    private lateinit var persistenceGateway: PersistenceGateway

    private lateinit var savePaymentUseCaseImpl: SavePaymentUseCaseImpl

    @Before
    fun setUp() {
        savePaymentUseCaseImpl = SavePaymentUseCaseImpl(persistenceGateway)
    }

    @Test
    fun createPayment() {
        whenever(persistenceGateway.findPayment(any(), any())).thenReturn(null)

        savePaymentUseCaseImpl.invoke(anyCents, anyPerson, anyExpenseId)

        verify(persistenceGateway).createPayment(any())
    }

    @Test
    fun updatePayment() {
        whenever(persistenceGateway.findPayment(any(), any())).thenReturn(Payment())

        savePaymentUseCaseImpl.invoke(anyCents, anyPerson, anyExpenseId)

        verify(persistenceGateway).updatePayment(any())
    }

    @Test
    fun deletePayment() {
        whenever(persistenceGateway.findPayment(any(), any())).thenReturn(Payment())

        savePaymentUseCaseImpl.invoke(0, anyPerson, anyExpenseId)

        verify(persistenceGateway).deletePayment(any(), any())
    }
}
