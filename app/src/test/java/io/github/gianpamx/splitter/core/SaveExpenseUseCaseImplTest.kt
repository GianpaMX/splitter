package io.github.gianpamx.splitter.core

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import io.github.gianpamx.splitter.core.entity.Expense
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SaveExpenseUseCaseImplTest {
    @Mock
    private lateinit var persistenceGateway: PersistenceGateway

    private lateinit var createExpenseUseCaseImpl: SaveExpenseUseCaseImpl

    @Before
    fun setUp() {
        createExpenseUseCaseImpl = SaveExpenseUseCaseImpl(persistenceGateway)
    }

    @Test
    fun createExpense() {

        createExpenseUseCaseImpl.invoke(Expense(title = "ANY_TITLE", description = "ANY_DESCRIPTION"))

        verify(persistenceGateway).createExpense(any())
    }
}
