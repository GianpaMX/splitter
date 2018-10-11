package io.github.gianpamx.splitter.gateway.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Expense")
data class ExpenseDBModel(
        @PrimaryKey(autoGenerate = true)
        var id: Long = 0,
        var title: String = "",
        var description: String = ""
)
