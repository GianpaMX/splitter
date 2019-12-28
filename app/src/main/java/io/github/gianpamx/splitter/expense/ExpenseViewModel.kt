package io.github.gianpamx.splitter.expense

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.github.gianpamx.splitter.AppSchedulers
import io.github.gianpamx.splitter.core.GetExpense
import io.github.gianpamx.splitter.core.GetPayers
import io.github.gianpamx.splitter.core.GetReceivers
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
  private val getPayers: GetPayers,
  private val getReceivers: GetReceivers,
  private val keepOrDeleteExpense: KeepOrDeleteExpense,
  private val saveExpense: SaveExpense,
  private val getExpense: GetExpense,
  private val appSchedulers: AppSchedulers
) : ViewModel() {

  val expense = MutableLiveData<ExpenseModel>()
  val payers = MutableLiveData<List<PayerModel>>()
  val receivers = MutableLiveData<List<ReceiverModel>>()
  val total = MutableLiveData<Double>()
  val error = MutableLiveData<Exception>()

  private val compositeDisposable = CompositeDisposable()

  fun loadExpense(expenseId: Long) {
    compositeDisposable.add(getExpense(expenseId)
        .subscribeOn(appSchedulers.computation())
        .observeOn(appSchedulers.mainThread())
        .subscribe({
          expense.value = it.toExpenseModel()
        }) {
          error.value = Exception(it)
        }
    )

    compositeDisposable.add(getPayers(expenseId)
        .subscribeOn(appSchedulers.computation())
        .observeOn(appSchedulers.mainThread())
        .subscribe({ output ->
          payers.value = output.payers.map { it.toPayerModel() }
          total.value = output.total.toAmount()
        }) {
          error.value = Exception(it)
        }
    )

    compositeDisposable.add(getReceivers(expenseId)
        .subscribeOn(appSchedulers.computation())
        .observeOn(appSchedulers.mainThread())
        .subscribe({ it ->
          receivers.value = it.map { it.toReceiverModel() }
        }) {
          error.value = Exception(it)
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
          error.value = Exception(it)
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
          error.value = Exception(it)
        }
    )
  }

  fun exitExpense(expenseId: Long) {
    compositeDisposable.add(keepOrDeleteExpense(expenseId)
        .subscribeOn(appSchedulers.computation())
        .observeOn(appSchedulers.mainThread())
        .subscribe({
          // ignore
        }) {
          error.value = Exception(it)
        }
    )
  }

  fun save(title: String, expenseId: Long) {
    compositeDisposable.add(getExpense(expenseId)
        .map { it.copy(title = title) }
        .flatMapSingle { saveExpense(it) }
        .subscribeOn(appSchedulers.computation())
        .observeOn(appSchedulers.mainThread())
        .subscribe({ _ ->
          // ignore
        }) {
          error.value = Exception(it)
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
}
