package io.github.gianpamx.splitter.gateway.room.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Index

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
