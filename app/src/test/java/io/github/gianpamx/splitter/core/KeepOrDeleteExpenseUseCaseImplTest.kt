package io.github.gianpamx.splitter.core

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.github.gianpamx.splitter.core.model.Expense
import io.github.gianpamx.splitter.core.model.Payment
import io.github.gianpamx.splitter.core.model.Person
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
