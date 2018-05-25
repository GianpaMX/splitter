package io.github.gianpamx.splitter.expense

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.github.gianpamx.splitter.core.*
import javax.inject.Inject
import kotlin.math.truncate

class ExpenseViewModel @Inject constructor(
        private val savePaymentUseCase: SavePaymentUseCase,
        private val observePayersUseCase: ObservePayersUseCase) : ViewModel() {

    val payers = MutableLiveData<List<PayerModel>>()
    val error = MutableLiveData<Exception>()

    fun observePayers(expenseId: Long) {
        observePayersUseCase.invoke(expenseId) {
            payers.postValue(it.map { it.toPayerModel() })
        }
    }

    fun save(payerModel: PayerModel, expenseId: Long) {
        try {
            savePaymentUseCase.invoke(payerModel.amount.toCents(), payerModel.toPerson(), expenseId)
        } catch (e: Exception) {
            error.postValue(e)
        }
    }
}

private fun Expense.toExpenseModel() = ExpenseModel(
        id = id,
        title = title,
        description = description
)

private fun Payment.toPayerModel() = PayerModel(
        id = person.id,
        name = person.name,
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
