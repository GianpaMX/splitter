package io.github.gianpamx.splitbetter.domain.dao

import io.github.gianpamx.splitbetter.domain.entity.Expense
import kotlinx.coroutines.flow.Flow

interface ExpenseDao {
    fun findAll(): Flow<List<Expense>>
    suspend fun save(newExpense: Expense)
    suspend fun find(expenseId: String): Expense?
}
