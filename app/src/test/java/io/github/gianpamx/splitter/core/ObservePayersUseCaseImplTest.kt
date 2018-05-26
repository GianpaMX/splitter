package io.github.gianpamx.splitter.core

import com.nhaarman.mockito_kotlin.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ObservePayersUseCaseImplTest {
    @Mock
    private lateinit var persistenceGateway: PersistenceGateway

    @Test
    fun listOfPayers() {
        val observer = mock<(List<Payer>) -> Unit>()
        argumentCaptor<(List<Payer>) -> Unit>().apply {
            whenever(persistenceGateway.observePayments(any(), capture())).then {
                firstValue.invoke(listOf(Payer()))
            }
        }
        val observePayersUseCaseImpl = ObservePayersUseCaseImpl(persistenceGateway)

        observePayersUseCaseImpl.invoke(1, observer)

        verify(observer).invoke(any())
    }
}
