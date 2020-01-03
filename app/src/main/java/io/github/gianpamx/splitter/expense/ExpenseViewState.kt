package io.github.gianpamx.splitter.expense

import io.github.gianpamx.splitter.expense.model.ExpenseModel
import io.github.gianpamx.splitter.expense.model.PayerModel
import io.github.gianpamx.splitter.expense.model.ReceiverModel

sealed class ExpenseViewState {
  class Ready(
    val expense: ExpenseModel,
    val payers: List<PayerModel>,
    val receivers: List<ReceiverModel>,
    val total: Double
  ) : ExpenseViewState()

  class Failure(val t: Throwable) : ExpenseViewState()
}
