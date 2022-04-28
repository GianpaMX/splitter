package io.github.gianpamx.splitbetter.domain.usecase

import io.github.gianpamx.splitbetter.domain.dao.ExpenseDao

class GetExpense(private val expenseDao: ExpenseDao) {
    suspend operator fun invoke(expenseId: String) = expenseDao.find(expenseId)
}
