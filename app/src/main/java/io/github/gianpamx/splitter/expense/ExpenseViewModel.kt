package io.github.gianpamx.splitter.expense

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.github.gianpamx.splitter.core.*
import javax.inject.Inject
import kotlin.math.truncate

class ExpenseViewModel @Inject constructor(
        private val savePaymentUseCase: SavePaymentUseCase,
        private val saveReceiverUseCase: SaveReceiverUseCase,
        private val observePayersUseCase: ObservePayersUseCase,
        private val observeReceiversUseCase: ObserveReceiversUseCase) : ViewModel() {

    val payers = MutableLiveData<List<PayerModel>>()
    val receivers = MutableLiveData<List<ReceiverModel>>()
    val error = MutableLiveData<Exception>()

    fun observePayers(expenseId: Long) {
        observePayersUseCase.invoke(expenseId) {
            payers.postValue(it.map { it.toPayerModel() })
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
}

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

private fun String.toCents() = try {
    truncate(toFloat() * 100).toInt()
} catch (t: Throwable) {
    0
}

private fun Int.toAmount() = if (this > 0) "${this.toFloat() / 100f}" else ""
