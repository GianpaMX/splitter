package io.github.gianpamx.splitbetter.domain.usecase

import io.github.gianpamx.splitbetter.domain.dao.ExpenseDao
import io.github.gianpamx.splitbetter.domain.entity.Expense
import io.github.gianpamx.splitbetter.domain.gateway.UniqueIdGateway

class SaveExpense(
    private val uniqueIdGateway: UniqueIdGateway,
    private val expenseDao: ExpenseDao,
) {
    suspend operator fun invoke(expense: Expense) {
        expenseDao.save(expense
            .takeUnless { it.id.isEmpty() }
            ?: expense.copy(id = uniqueIdGateway.getUniqueId())
        )
    }
}
