package io.github.gianpamx.splitter.core

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.github.gianpamx.splitter.core.model.Person
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

private const val anyPersonId = 1L
private const val anyExpenseId = 1L
private val anyPerson = Person(id = anyPersonId)

@RunWith(MockitoJUnitRunner::class)
class SaveReceiverUseCaseImplTest {
    @Mock
    private lateinit var persistenceGateway: PersistenceGateway

    private lateinit var saveReceiverUseCaseImpl: SaveReceiverUseCaseImpl

    @Before
    fun setUp() {
        saveReceiverUseCaseImpl = SaveReceiverUseCaseImpl(persistenceGateway)
    }

    @Test
    fun createPayment() {
        whenever(persistenceGateway.findReceiver(any(), any())).thenReturn(null)

        saveReceiverUseCaseImpl.invoke(true, anyPerson, anyExpenseId)

        verify(persistenceGateway).createReceiver(any(), any())
    }

    @Test
    fun deletePayment() {
        whenever(persistenceGateway.findReceiver(any(), any())).thenReturn(Pair(anyPersonId, anyExpenseId))

        saveReceiverUseCaseImpl.invoke(false, anyPerson, anyExpenseId)

        verify(persistenceGateway).deleteReceiver(any(), any())
    }
}
