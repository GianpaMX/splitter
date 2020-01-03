package io.github.gianpamx.splitter.expense

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.github.gianpamx.splitter.AppSchedulers
import io.github.gianpamx.splitter.core.GetExpense
import io.github.gianpamx.splitter.core.GetExpense.Output
import io.github.gianpamx.splitter.core.KeepOrDeleteExpense
import io.github.gianpamx.splitter.core.SaveExpense
import io.github.gianpamx.splitter.core.SavePayment
import io.github.gianpamx.splitter.core.SaveReceiver
import io.github.gianpamx.splitter.core.entity.Expense
import io.github.gianpamx.splitter.core.entity.Payer
import io.github.gianpamx.splitter.core.entity.Person
import io.github.gianpamx.splitter.core.toAmount
import io.github.gianpamx.splitter.core.toCents
import io.github.gianpamx.splitter.expense.model.ExpenseModel
import io.github.gianpamx.splitter.expense.model.PayerModel
import io.github.gianpamx.splitter.expense.model.ReceiverModel
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class ExpenseViewModel @Inject constructor(
  private val savePayment: SavePayment,
  private val saveReceiver: SaveReceiver,
  private val keepOrDeleteExpense: KeepOrDeleteExpense,
  private val saveExpense: SaveExpense,
  private val getExpense: GetExpense,
  private val appSchedulers: AppSchedulers
) : ViewModel() {

  val viewState = MutableLiveData<ExpenseViewState>()

  private val compositeDisposable = CompositeDisposable()

  fun loadExpense(expenseId: Long) {
    compositeDisposable.add(getExpense(expenseId)
        .subscribeOn(appSchedulers.computation())
        .observeOn(appSchedulers.mainThread())
        .subscribe({
          viewState.value = it.toExpenseViewState()
        }) {
          viewState.value = ExpenseViewState.Failure(it)
        }
    )
  }

  fun save(payerModel: PayerModel, expenseId: Long) {
    compositeDisposable.add(savePayment(payerModel.amount.toCents(), payerModel.toPerson(), expenseId)
        .subscribeOn(appSchedulers.computation())
        .observeOn(appSchedulers.mainThread())
        .subscribe({
          // ignore
        }) {
          viewState.value = ExpenseViewState.Failure(it)
        }
    )
  }

  fun save(receiverModel: ReceiverModel, expenseId: Long) {
    compositeDisposable.add(saveReceiver(receiverModel.isChecked, receiverModel.toPerson(), expenseId)
        .subscribeOn(appSchedulers.computation())
        .observeOn(appSchedulers.mainThread())
        .subscribe({
          // ignore
        }) {
          viewState.value = ExpenseViewState.Failure(it)
        }
    )
  }

  fun exitExpense(expenseId: Long, title: String) {
    compositeDisposable.add(getExpense(expenseId)
        .map { it.expense.copy(title = title) }
        .flatMapSingle { saveExpense(it) }
        .flatMapCompletable {
          keepOrDeleteExpense(expenseId)
        }
        .subscribeOn(appSchedulers.computation())
        .observeOn(appSchedulers.mainThread())
        .subscribe({
          // ignore
        }) {
          viewState.value = ExpenseViewState.Failure(it)
        }
    )
  }

  override fun onCleared() {
    compositeDisposable.clear()
  }

  private fun Expense.toExpenseModel() = ExpenseModel(
      id = id,
      title = title,
      description = description
  )

  private fun ReceiverModel.toPerson() = Person(
      id = id,
      name = name
  )

  private fun Pair<Person, Boolean>.toReceiverModel() = ReceiverModel(
      first.id,
      first.name,
      second
  )

  private fun Payer.toPayerModel() = PayerModel(
      id = personId,
      name = name,
      amount = cents.toAmount()
  )

  private fun PayerModel.toPerson() = Person(
      id = id,
      name = name
  )

  private fun Output.toExpenseViewState() = ExpenseViewState.Ready(
      expense = this.expense.toExpenseModel(),
      payers = this.payers.map { it.toPayerModel() },
      receivers = this.receivers.map { it.toReceiverModel() },
      total = this.total.toAmount()
  )
}
