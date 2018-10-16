package io.github.gianpamx.splitter.gateway.room.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(tableName = "Payment",
        indices = [Index(value = ["expenseId", "personId"], unique = true)],
        foreignKeys = [
            ForeignKey(
                    entity = ExpenseDBModel::class,
                    parentColumns = ["id"],
                    childColumns = ["expenseId"]
            ),
            ForeignKey(
                    entity = PersonDBModel::class,
                    parentColumns = ["id"],
                    childColumns = ["personId"]
            )
        ],
        primaryKeys = ["expenseId", "personId"])
data class PaymentDBModel(
        var expenseId: Long = 0,
        var personId: Long = 0,
        var cents: Int = 0
)
