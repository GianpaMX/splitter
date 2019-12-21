package io.github.gianpamx.splitter.expense

import io.github.gianpamx.splitter.expense.model.PayerModel
import io.github.gianpamx.splitter.expense.model.ReceiverModel

sealed class ExpenseViewState {
  class Ready(
    expense: ExpenseViewModel,
    payers: List<PayerModel>,
    receivers: List<ReceiverModel>,
    total: Double
  ) : ExpenseViewState()

  class Error(t: Throwable)
}
