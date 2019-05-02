package io.github.gianpamx.splitter.core

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import io.github.gianpamx.splitter.core.model.Expense
import org.hamcrest.core.IsEqual
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

private const val ANY_EXPENSE_ID = 1L

@RunWith(MockitoJUnitRunner::class)
class GetExpenseUseCaseImplTest {
    @Mock
    private lateinit var persistenceGateway: PersistenceGateway

    private lateinit var getExpenseUseCaseImpl: GetExpenseUseCaseImpl

    @Before
    fun setUp() {
        getExpenseUseCaseImpl = GetExpenseUseCaseImpl(persistenceGateway)
    }

    @Test
    fun existingExpense() {
        whenever(persistenceGateway.findExpense(any())).thenReturn(Expense())

        val expense = getExpenseUseCaseImpl.invoke(ANY_EXPENSE_ID)

        assertThat(expense, IsEqual(Expense()))
    }
}
