package io.github.gianpamx.splitter.expense

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.github.gianpamx.splitter.core.GetExpense
import io.github.gianpamx.splitter.core.KeepOrDeleteExpenseUseCase
import io.github.gianpamx.splitter.core.ObservePayersUseCase
import io.github.gianpamx.splitter.core.ObserveReceiversUseCase
import io.github.gianpamx.splitter.core.SaveExpense
import io.github.gianpamx.splitter.core.SavePaymentUseCase
import io.github.gianpamx.splitter.core.SaveReceiverUseCase
import io.github.gianpamx.splitter.core.entity.Expense
import io.github.gianpamx.splitter.core.entity.Payer
import io.github.gianpamx.splitter.expense.model.PayerModel
import io.reactivex.Maybe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.hamcrest.Matchers.equalTo
import org.hamcrest.collection.IsIn
import org.hamcrest.core.IsInstanceOf
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

private const val anyExpenseId = 1L

@Ignore
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ExpenseViewModelTest {
  private val mainThreadSurrogate = newSingleThreadContext("UI thread")

  @Rule @JvmField val instantExecutor = InstantTaskExecutorRule()

  @Mock private lateinit var savePaymentUseCase: SavePaymentUseCase

  @Mock private lateinit var saveReceiverUseCase: SaveReceiverUseCase

  @Mock private lateinit var observePayersUseCase: ObservePayersUseCase

  @Mock private lateinit var observeReceiversUseCase: ObserveReceiversUseCase

  @Mock private lateinit var keepOrDeleteExpenseUseCase: KeepOrDeleteExpenseUseCase

  @Mock private lateinit var saveExpense: SaveExpense

  @Mock private lateinit var getExpense: GetExpense

  private lateinit var expenseViewModel: ExpenseViewModel

  @Before
  fun setUp() {
    Dispatchers.setMain(mainThreadSurrogate)

    whenever(getExpense.invoke(any())).thenReturn(Maybe.just(Expense()))

    expenseViewModel = ExpenseViewModel(
        savePaymentUseCase,
        saveReceiverUseCase,
        observePayersUseCase,
        observeReceiversUseCase,
        keepOrDeleteExpenseUseCase,
        saveExpense,
        getExpense
    )
  }

  @After
  fun tearDown() {
    Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
    mainThreadSurrogate.close()
  }

  @Test
  fun saveNewPayer() = runBlocking<Unit> {
    val expectedPayer = PayerModel(name = "ANY_NAME", amount = 0.1 + 0.2)

    expenseViewModel.save(expectedPayer, anyExpenseId)

    verify(savePaymentUseCase).invoke(eq(30), any(), any())
  }

  @Test
  fun errorSavingAPayer() = runBlocking {
    whenever(savePaymentUseCase.invoke(any(), any(), any())).thenThrow(Exception("ANY_EXCEPTION"))

    expenseViewModel.save(PayerModel(), anyExpenseId)

    assertThat(expenseViewModel.error.value, IsInstanceOf(Exception::class.java))
  }

  @Test
  fun onNewPayerInserted() = runBlocking {
    argumentCaptor<(List<Payer>, Int) -> Unit>().apply {
      whenever(observePayersUseCase.invoke(any(), capture())).then {
        firstValue.invoke(listOf(Payer(cents = 12345, personId = 1)), 12345)
      }
    }

    expenseViewModel.loadExpense(anyExpenseId)

    assertThat(PayerModel(id = 1, amount = 123.45), IsIn(expenseViewModel.payers.value!!))
  }

  @Test
  fun point1pluspoint2equalspoint3() = runBlocking {
    argumentCaptor<(List<Payer>, Int) -> Unit>().apply {
      whenever(observePayersUseCase.invoke(any(), capture())).then {
        firstValue.invoke(listOf(Payer(cents = 10), Payer(cents = 20)), 30)
      }
    }

    expenseViewModel.loadExpense(anyExpenseId)

    assertThat(expenseViewModel.total.value, equalTo(0.3))
  }

  @Test
  fun createExpense() = runBlocking<Unit> {
    expenseViewModel.save("ANY TITLE", anyExpenseId)

    verify(saveExpense).invoke(any())
  }
}
