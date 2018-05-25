package io.github.gianpamx.splitter.gateway.room.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "Expense")
data class ExpenseDBModel(
        @PrimaryKey(autoGenerate = true)
        var id: Long = 0,
        var title: String = "",
        var description: String = ""
)
