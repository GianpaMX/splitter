package io.github.gianpamx.splitter.expense

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.github.gianpamx.splitter.core.*
import io.github.gianpamx.splitter.core.model.Expense
import io.github.gianpamx.splitter.core.model.Payer
import io.github.gianpamx.splitter.core.model.Person
import io.github.gianpamx.splitter.expense.model.ExpenseModel
import io.github.gianpamx.splitter.expense.model.PayerModel
import io.github.gianpamx.splitter.expense.model.ReceiverModel
import javax.inject.Inject

class ExpenseViewModel @Inject constructor(
        private val savePaymentUseCase: SavePaymentUseCase,
        private val saveReceiverUseCase: SaveReceiverUseCase,
        private val observePayersUseCase: ObservePayersUseCase,
        private val observeReceiversUseCase: ObserveReceiversUseCase,
        private val keepOrDeleteExpenseUseCase: KeepOrDeleteExpenseUseCase,
        private val saveExpenseUseCase: SaveExpenseUseCase,
        private val getExpenseUseCase: GetExpenseUseCase) : ViewModel() {

    val expense = MutableLiveData<ExpenseModel>()
    val payers = MutableLiveData<List<PayerModel>>()
    val receivers = MutableLiveData<List<ReceiverModel>>()
    val total = MutableLiveData<Double>()
    val error = MutableLiveData<Exception>()

    fun loadExpense(expenseId: Long) {
        expense.postValue(getExpenseUseCase.invoke(expenseId).toExpenseModel())

        observePayersUseCase.invoke(expenseId) { payers, total ->
            this.payers.postValue(payers.map { it.toPayerModel() })
            this.total.postValue(total.toAmount())
        }

        observeReceiversUseCase.invoke(expenseId) {
            receivers.postValue(it.map { it.toReceiverModel() })
        }
    }

    fun save(payerModel: PayerModel, expenseId: Long) {
        try {
            savePaymentUseCase.invoke(payerModel.amount.toCents(), payerModel.toPerson(), expenseId)
        } catch (e: Exception) {
            error.postValue(e)
        }
    }

    fun save(receiverModel: ReceiverModel, expenseId: Long) {
        try {
            saveReceiverUseCase.invoke(receiverModel.isChecked, receiverModel.toPerson(), expenseId)
        } catch (e: Exception) {
            error.postValue(e)
        }
    }

    fun exitExpense(expenseId: Long) {
        keepOrDeleteExpenseUseCase.invoke(expenseId)
    }

    fun save(title: String, expenseId: Long) {
        val expense = getExpenseUseCase.invoke(expenseId)
        expense.title = title
        saveExpenseUseCase.invoke(expense)
    }
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
