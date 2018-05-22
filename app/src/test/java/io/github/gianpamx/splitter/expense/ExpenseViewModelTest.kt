package io.github.gianpamx.splitter.expense

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.whenever
import io.github.gianpamx.splitter.core.Payer
import io.github.gianpamx.splitter.core.SavePayerUseCase
import org.hamcrest.collection.IsIn
import org.hamcrest.core.IsInstanceOf
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ExpenseViewModelTest {
    @Rule
    @JvmField
    val instantExecutor = InstantTaskExecutorRule()

    @Mock
    private lateinit var mock: SavePayerUseCase

    private lateinit var expenseViewModel: ExpenseViewModel

    @Before
    fun setUp() {
        expenseViewModel = ExpenseViewModel(mock)
    }

    @Test
    fun saveNewPayer() {
        val expectedPayer = PayerModel(name = "ANY_NAME", amount = "$ 123.45")
        whenever(mock.invoke(any())).thenReturn(listOf(Payer(name = "ANY_NAME", cents = 12345)))

        expenseViewModel.save(expectedPayer)

        assertThat(expectedPayer, IsIn(expenseViewModel.payers.value!!))
    }

    @Test
    fun errorSavingAPayer() {
        whenever(mock.invoke(any())).thenThrow(Exception("ANY_EXCEPTION"))

        expenseViewModel.save(PayerModel())

        assertThat(expenseViewModel.error.value, IsInstanceOf(Exception::class.java))
    }
}
