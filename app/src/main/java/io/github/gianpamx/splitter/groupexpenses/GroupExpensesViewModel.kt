package io.github.gianpamx.splitter.groupexpenses

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.github.gianpamx.splitter.AppSchedulers
import io.github.gianpamx.splitter.core.ObserveExpenses
import io.github.gianpamx.splitter.core.SaveExpense
import io.github.gianpamx.splitter.core.entity.Expense
import io.github.gianpamx.splitter.core.toAmount
import io.github.gianpamx.splitter.groupexpenses.model.ExpenseItem
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import javax.inject.Inject

class GroupExpensesViewModel @Inject constructor(
  private val saveExpense: SaveExpense,
  observeExpenses: ObserveExpenses,
  private val appSchedulers: AppSchedulers
) : ViewModel() {
  val viewState = MutableLiveData<ExpensesViewState>()

  private val compositeDisposable = CompositeDisposable()

  init {
    compositeDisposable.add(observeExpenses()
        .subscribeOn(appSchedulers.computation())
        .observeOn(appSchedulers.mainThread())
        .subscribe { it ->
          viewState.value = it.toViewState()
        })
  }

  fun createExpense() {
    compositeDisposable.add(
        saveExpense(Expense())
            .subscribeOn(appSchedulers.computation())
            .observeOn(appSchedulers.mainThread())
            .subscribe(Consumer {
              viewState.value = ExpensesViewState.NewExpense(it.id)
            })
    )
  }

  override fun onCleared() {
    compositeDisposable.clear()
  }

  private fun ObserveExpenses.Output.toViewState() = ExpensesViewState.Ready(
      expenses = this.expenses.map { it.toExpenseItem() },
      total = this.total.toAmount()
  )

  private fun ObserveExpenses.TotalExpense.toExpenseItem() = ExpenseItem(
      id = this.id,
      title = this.title,
      total = this.total.toAmount()
  )
}
