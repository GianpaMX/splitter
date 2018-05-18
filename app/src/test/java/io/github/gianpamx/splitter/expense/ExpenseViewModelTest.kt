package io.github.gianpamx.splitter.expense

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argumentCaptor
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
    private lateinit var savePayerUseCase: SavePayerUseCase

    private lateinit var expenseViewModel: ExpenseViewModel

    @Before
    fun setUp() {
        expenseViewModel = ExpenseViewModel(savePayerUseCase)
    }

    @Test
    fun saveNewPlayer() {
        val expectedPayer = PayerModel(name = "ANY_NAME", amount = "123.45")
        with(argumentCaptor<(List<Payer>) -> Unit>()) {
            whenever(savePayerUseCase.invoke(any(), capture(), any())).then {
                firstValue.invoke(listOf(Payer(name = "ANY_NAME", cents = 12345)))
            }
        }

        expenseViewModel.save(expectedPayer)

        assertThat(expectedPayer, IsIn(expenseViewModel.payers.value!!))
    }

    @Test
    fun errorSavingAPayer() {
        with(argumentCaptor<(Exception) -> Unit>()) {
            whenever(savePayerUseCase.invoke(any(), any(), capture())).then {
                firstValue.invoke(Exception("ANY EXCEPTION"))
            }
        }

        expenseViewModel.save(PayerModel())

        assertThat(expenseViewModel.error.value, IsInstanceOf(Exception::class.java))
    }
}
