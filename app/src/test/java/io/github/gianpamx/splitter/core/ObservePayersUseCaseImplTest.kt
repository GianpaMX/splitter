package io.github.gianpamx.splitter.core

import com.nhaarman.mockitokotlin2.*
import io.github.gianpamx.splitter.core.model.Payer
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
        val observer = mock<(List<Payer>, Int) -> Unit>()
        argumentCaptor<(List<Payer>) -> Unit>().apply {
            whenever(persistenceGateway.observePayments(any(), capture())).then {
                firstValue.invoke(listOf(Payer(cents = 1), Payer(cents = 2)))
            }
        }
        val observePayersUseCaseImpl = ObservePayersUseCaseImpl(persistenceGateway)

        observePayersUseCaseImpl.invoke(1, observer)

        verify(observer).invoke(any(), eq(3))
    }
}
