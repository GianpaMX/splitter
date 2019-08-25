package io.github.gianpamx.splitter.frameworks.room

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.gianpamx.splitter.frameworks.room.model.ExpenseDBModel
import io.github.gianpamx.splitter.frameworks.room.model.PaymentDBModel
import io.github.gianpamx.splitter.frameworks.room.model.PersonDBModel
import io.github.gianpamx.splitter.frameworks.room.model.ReceiverDBModel

@Database(
        entities = [
            PersonDBModel::class,
            ExpenseDBModel::class,
            PaymentDBModel::class,
            ReceiverDBModel::class
        ],
        version = 1,
        exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dao(): DatabaseDao
}
