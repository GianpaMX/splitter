package io.github.gianpamx.splitter.expense

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.github.gianpamx.splitter.core.*
import javax.inject.Inject
import kotlin.math.roundToInt

class ExpenseViewModel @Inject constructor(
        private val savePaymentUseCase: SavePaymentUseCase,
        private val saveReceiverUseCase: SaveReceiverUseCase,
        private val observePayersUseCase: ObservePayersUseCase,
        private val observeReceiversUseCase: ObserveReceiversUseCase,
        private val keepOrDeleteExpenseUseCase: KeepOrDeleteExpenseUseCase,
        private val saveExpenseUseCase: SaveExpenseUseCase) : ViewModel() {

    val payers = MutableLiveData<List<PayerModel>>()
    val receivers = MutableLiveData<List<ReceiverModel>>()
    val total = MutableLiveData<Double>()
    val error = MutableLiveData<Exception>()

    fun observePayersAndReceivers(expenseId: Long) {
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

    fun save(expenseModel: ExpenseModel) {
        saveExpenseUseCase.invoke(expenseModel.toExpense())
    }
}

private fun ExpenseModel.toExpense() = Expense(
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

private fun Double.toCents() = try {
    (this * 100.0).roundToInt()
} catch (t: Throwable) {
    0
}

private fun Int.toAmount(): Double = this.toDouble() / 100.0
