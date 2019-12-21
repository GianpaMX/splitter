package io.github.gianpamx.splitter.groupexpenses

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import io.github.gianpamx.splitter.TestSchedulers
import io.github.gianpamx.splitter.core.ObserveExpenses
import io.github.gianpamx.splitter.core.ObserveExpenses.Output
import io.github.gianpamx.splitter.core.SaveExpense
import io.github.gianpamx.splitter.core.entity.Expense
import io.reactivex.Flowable
import io.reactivex.Single
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GroupExpensesViewModelTest {
  @get:Rule var rule: TestRule = InstantTaskExecutorRule()

  @Mock lateinit var saveExpense: SaveExpense

  @Mock lateinit var observeExpenses: ObserveExpenses

  lateinit var testSchedulers: TestSchedulers

  lateinit var viewModel: GroupExpensesViewModel

  @Before fun setUp() {
    testSchedulers = TestSchedulers()
    whenever(observeExpenses.invoke()).thenReturn(Flowable.never())

    viewModel = GroupExpensesViewModel(saveExpense, observeExpenses, testSchedulers)
  }

  @Test fun `ready state for group of expenses`() {
    whenever(observeExpenses.invoke()).thenReturn(Flowable.just(Output(listOf(ObserveExpenses.TotalExpense()))))

    viewModel = GroupExpensesViewModel(saveExpense, observeExpenses, testSchedulers)

    assertThat(viewModel.viewState.value).isInstanceOf(ExpensesViewState.Ready::class.java)
  }

  @Test fun `new expense`() {
    whenever(saveExpense.invoke(any())).thenReturn(Single.just(Expense()))

    viewModel.createExpense()

    assertThat(viewModel.viewState.value).isInstanceOf(ExpensesViewState.NewExpense::class.java)
  }
}
