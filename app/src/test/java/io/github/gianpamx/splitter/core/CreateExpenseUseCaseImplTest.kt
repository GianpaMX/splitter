package io.github.gianpamx.splitter.core

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CreateExpenseUseCaseImplTest {
    @Mock
    private lateinit var persistenceGateway: PersistenceGateway

    private lateinit var createExpenseUseCaseImpl: CreateExpenseUseCaseImpl

    @Before
    fun setUp() {
        createExpenseUseCaseImpl = CreateExpenseUseCaseImpl(persistenceGateway)
    }

    @Test
    fun createExpense() {

        createExpenseUseCaseImpl.invoke("ANY_TITLE", "ANY_DESCRIPTION")

        verify(persistenceGateway).createExpense(any(), any())
    }
}
