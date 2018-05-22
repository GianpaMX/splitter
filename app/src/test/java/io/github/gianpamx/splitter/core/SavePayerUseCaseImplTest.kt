package io.github.gianpamx.splitter.core

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

private val anyPayer = Payer(id = 1)

@RunWith(MockitoJUnitRunner::class)
class SavePayerUseCaseImplTest {
    @Mock
    private lateinit var persistenceGateway: PersistenceGateway

    private lateinit var savePayerUseCaseImpl: SavePayerUseCaseImpl

    @Before
    fun setUp() {
        savePayerUseCaseImpl = SavePayerUseCaseImpl(persistenceGateway)
    }

    @Test
    fun createPayer() {
        whenever(persistenceGateway.findAllPayers()).thenReturn(listOf(anyPayer))

        savePayerUseCaseImpl.invoke(Payer())

        verify(persistenceGateway).create(any())
    }

    @Test
    fun updatePayer() {
        whenever(persistenceGateway.findAllPayers()).thenReturn(listOf(anyPayer))

        savePayerUseCaseImpl.invoke(anyPayer)

        verify(persistenceGateway).update(any())
    }
}
