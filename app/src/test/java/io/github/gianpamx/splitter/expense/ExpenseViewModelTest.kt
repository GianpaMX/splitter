package io.github.gianpamx.splitter.expense

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.github.gianpamx.splitter.core.ObservePayersUseCase
import io.github.gianpamx.splitter.core.ObserveReceiversUseCase
import io.github.gianpamx.splitter.core.Payer
import io.github.gianpamx.splitter.core.SavePaymentUseCase
import org.hamcrest.collection.IsIn
import org.hamcrest.core.IsInstanceOf
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

private const val anyExpenseId = 1L

@RunWith(MockitoJUnitRunner::class)
class ExpenseViewModelTest {
    @Rule
    @JvmField
    val instantExecutor = InstantTaskExecutorRule()

    @Mock
    private lateinit var savePaymentUseCase: SavePaymentUseCase

    @Mock
    private lateinit var observePayersUseCase: ObservePayersUseCase

    @Mock
    private lateinit var observeReceiversUseCase: ObserveReceiversUseCase

    private lateinit var expenseViewModel: ExpenseViewModel

    @Before
    fun setUp() {
        expenseViewModel = ExpenseViewModel(savePaymentUseCase, observePayersUseCase, observeReceiversUseCase)
    }

    @Test
    fun saveNewPayer() {
        val expectedPayer = PayerModel(name = "ANY_NAME", amount = "123.45")

        expenseViewModel.save(expectedPayer, anyExpenseId)

        verify(savePaymentUseCase).invoke(any(), any(), any())
    }

    @Test
    fun errorSavingAPayer() {
        whenever(savePaymentUseCase.invoke(any(), any(), any())).thenThrow(Exception("ANY_EXCEPTION"))

        expenseViewModel.save(PayerModel(), anyExpenseId)

        assertThat(expenseViewModel.error.value, IsInstanceOf(Exception::class.java))
    }

    @Test
    fun onNewPayerInserted() {
        argumentCaptor<(List<Payer>) -> Unit>().apply {
            whenever(observePayersUseCase.invoke(any(), capture())).then {
                firstValue.invoke(listOf(Payer(cents = 12345, personId = 1)))
            }
        }

        expenseViewModel.observePayers(anyExpenseId)

        assertThat(PayerModel(id = 1, amount = "123.45"), IsIn(expenseViewModel.payers.value!!))
    }
}
