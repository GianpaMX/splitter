package io.github.gianpamx.splitter.expense

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import io.github.gianpamx.splitter.TestSchedulers
import io.github.gianpamx.splitter.core.GetExpense
import io.github.gianpamx.splitter.core.GetExpense.Output
import io.github.gianpamx.splitter.core.GetPayers
import io.github.gianpamx.splitter.core.GetReceivers
import io.github.gianpamx.splitter.core.KeepOrDeleteExpense
import io.github.gianpamx.splitter.core.SaveExpense
import io.github.gianpamx.splitter.core.SavePayment
import io.github.gianpamx.splitter.core.SaveReceiver
import io.github.gianpamx.splitter.core.entity.Expense
import io.reactivex.Observable
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

private const val ANY_EXPENSE_ID = 1L

@RunWith(MockitoJUnitRunner::class)
class ExpenseViewModelTest {
  @get:Rule var rule: TestRule = InstantTaskExecutorRule()

  @Mock lateinit var savePayment: SavePayment
  @Mock lateinit var saveReceiver: SaveReceiver
  @Mock lateinit var getPayers: GetPayers
  @Mock lateinit var getReceivers: GetReceivers
  @Mock lateinit var keepOrDeleteExpense: KeepOrDeleteExpense
  @Mock lateinit var saveExpense: SaveExpense
  @Mock lateinit var getExpense: GetExpense

  lateinit var testSchedulers: TestSchedulers

  lateinit var viewModel: ExpenseViewModel

  @Before fun setUp() {
    testSchedulers = TestSchedulers()

    viewModel = ExpenseViewModel(savePayment, saveReceiver, keepOrDeleteExpense, saveExpense, getExpense, testSchedulers)
  }

  @Test fun `load expense successfully`() {
    val emptyOutput = Output(expense = Expense(), payers = emptyList(), receivers = emptyList(), total = 0)
    whenever(getExpense.invoke(any())).thenReturn(Observable.just(emptyOutput))

    viewModel.loadExpense(ANY_EXPENSE_ID)

    assertThat(viewModel.viewState.value).isInstanceOf(ExpenseViewState.Ready::class.java)
  }
}
