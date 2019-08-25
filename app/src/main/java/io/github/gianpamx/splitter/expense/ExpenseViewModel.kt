package io.github.gianpamx.splitter.expense

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.gianpamx.splitter.core.*
import io.github.gianpamx.splitter.core.entity.Expense
import io.github.gianpamx.splitter.core.entity.Payer
import io.github.gianpamx.splitter.core.entity.Person
import io.github.gianpamx.splitter.expense.model.ExpenseModel
import io.github.gianpamx.splitter.expense.model.PayerModel
import io.github.gianpamx.splitter.expense.model.ReceiverModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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

    fun loadExpense(expenseId: Long) = viewModelScope.launch(Dispatchers.Default) {
        expense.postValue(getExpenseUseCase.invoke(expenseId).toExpenseModel())

        observePayersUseCase.invoke(expenseId) { payers, total ->
            this@ExpenseViewModel.payers.postValue(payers.map { it.toPayerModel() })
            this@ExpenseViewModel.total.postValue(total.toAmount())
        }

        observeReceiversUseCase.invoke(expenseId) {
            receivers.postValue(it.map { it.toReceiverModel() })
        }
    }


    fun save(payerModel: PayerModel, expenseId: Long) = viewModelScope.launch(Dispatchers.IO) {
        try {
            savePaymentUseCase.invoke(payerModel.amount.toCents(), payerModel.toPerson(), expenseId)
        } catch (e: Exception) {
            error.postValue(e)
        }
    }

    fun save(receiverModel: ReceiverModel, expenseId: Long) = viewModelScope.launch(Dispatchers.IO) {
        try {
            saveReceiverUseCase.invoke(receiverModel.isChecked, receiverModel.toPerson(), expenseId)
        } catch (e: Exception) {
            error.postValue(e)
        }
    }

    fun exitExpense(expenseId: Long) = viewModelScope.launch(Dispatchers.IO) {
        keepOrDeleteExpenseUseCase.invoke(expenseId)
    }

    fun save(title: String, expenseId: Long) = viewModelScope.launch(Dispatchers.IO) {
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
