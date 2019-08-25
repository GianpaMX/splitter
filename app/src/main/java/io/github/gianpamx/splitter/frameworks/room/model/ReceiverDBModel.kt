package io.github.gianpamx.splitter.frameworks.room.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(tableName = "Receiver",
        indices = [(Index(value = ["expenseId", "personId"], unique = true))],
        foreignKeys = [
            (ForeignKey(
                    entity = ExpenseDBModel::class,
                    parentColumns = ["id"],
                    childColumns = ["expenseId"]
            )),
            (ForeignKey(
                    entity = PersonDBModel::class,
                    parentColumns = ["id"],
                    childColumns = ["personId"]
            ))
        ],
        primaryKeys = ["expenseId", "personId"]
)
data class ReceiverDBModel(
        var expenseId: Long = 0,
        var personId: Long = 0
)
