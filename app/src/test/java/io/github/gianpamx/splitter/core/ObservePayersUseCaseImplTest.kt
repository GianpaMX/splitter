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
    fun empty() {
        val observer = mock<(List<Payment>) -> Unit>()
        argumentCaptor<(List<Payment>) -> Unit>().apply {
            whenever(persistenceGateway.observePayments(any(), capture())).then {
                firstValue.invoke(listOf(Payment()))
            }
        }
        val observePayersUseCaseImpl = ObservePayersUseCaseImpl(persistenceGateway)

        observePayersUseCaseImpl.invoke(1, observer)

        verify(observer).invoke(any())
    }
}
