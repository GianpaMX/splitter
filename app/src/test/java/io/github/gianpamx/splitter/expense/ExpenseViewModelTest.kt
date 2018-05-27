package io.github.gianpamx.splitter.expense

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockito_kotlin.*
import io.github.gianpamx.splitter.core.*
import org.hamcrest.Matchers.equalTo
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
    private lateinit var saveReceiverUseCase: SaveReceiverUseCase

    @Mock
    private lateinit var observePayersUseCase: ObservePayersUseCase

    @Mock
    private lateinit var observeReceiversUseCase: ObserveReceiversUseCase

    private lateinit var expenseViewModel: ExpenseViewModel

    @Before
    fun setUp() {
        expenseViewModel = ExpenseViewModel(savePaymentUseCase, saveReceiverUseCase, observePayersUseCase, observeReceiversUseCase)
    }

    @Test
    fun saveNewPayer() {
        val expectedPayer = PayerModel(name = "ANY_NAME", amount = 0.1 + 0.2)

        expenseViewModel.save(expectedPayer, anyExpenseId)

        verify(savePaymentUseCase).invoke(eq(30), any(), any())
    }

    @Test
    fun errorSavingAPayer() {
        whenever(savePaymentUseCase.invoke(any(), any(), any())).thenThrow(Exception("ANY_EXCEPTION"))

        expenseViewModel.save(PayerModel(), anyExpenseId)

        assertThat(expenseViewModel.error.value, IsInstanceOf(Exception::class.java))
    }

    @Test
    fun onNewPayerInserted() {
        argumentCaptor<(List<Payer>, Int) -> Unit>().apply {
            whenever(observePayersUseCase.invoke(any(), capture())).then {
                firstValue.invoke(listOf(Payer(cents = 12345, personId = 1)), 12345)
            }
        }

        expenseViewModel.observePayersAndReceivers(anyExpenseId)

        assertThat(PayerModel(id = 1, amount = 123.45), IsIn(expenseViewModel.payers.value!!))
    }

    @Test
    fun point1pluspoint2equalspoint3() {
        argumentCaptor<(List<Payer>, Int) -> Unit>().apply {
            whenever(observePayersUseCase.invoke(any(), capture())).then {
                firstValue.invoke(listOf(Payer(cents = 10), Payer(cents = 20)), 30)
            }
        }

        expenseViewModel.observePayersAndReceivers(anyExpenseId)

        assertThat(expenseViewModel.total.value, equalTo(0.3))
    }
}
