package io.github.gianpamx.splitbetter.domain.usecase

import io.github.gianpamx.splitbetter.domain.dao.ExpenseDao

class GetExpenses(private val expenseDao: ExpenseDao) {
    operator fun invoke() = expenseDao.findAll()
}
