package io.github.gianpamx.splitter.core

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

private const val ANY_EXPENSE_ID = 1L

@RunWith(MockitoJUnitRunner::class)
class KeepOrDeleteExpenseUseCaseImplTest {
    @Mock
    private lateinit var persistenceGateway: PersistenceGateway

    lateinit var keepOrDeleteExpenseUseCaseImpl: KeepOrDeleteExpenseUseCaseImpl

    @Before
    fun setUp() {
        keepOrDeleteExpenseUseCaseImpl = KeepOrDeleteExpenseUseCaseImpl(persistenceGateway)
        whenever(persistenceGateway.findExpense(any())).thenReturn(Expense())
    }

    @Test
    fun keepExpense() {
        whenever(persistenceGateway.findReceivers(any())).thenReturn(listOf(Person()))
        whenever(persistenceGateway.findPayments(any())).thenReturn(listOf(Payment()))

        keepOrDeleteExpenseUseCaseImpl.invoke(ANY_EXPENSE_ID)

        verify(persistenceGateway, times(0)).deleteExpense(any())
    }

    @Test
    fun deleteExpense() {
        whenever(persistenceGateway.findReceivers(any())).thenReturn(emptyList())
        whenever(persistenceGateway.findPayments(any())).thenReturn(emptyList())

        keepOrDeleteExpenseUseCaseImpl.invoke(ANY_EXPENSE_ID)

        verify(persistenceGateway).deleteExpense(any())
    }
}
