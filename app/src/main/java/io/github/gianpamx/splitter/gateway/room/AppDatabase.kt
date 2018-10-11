package io.github.gianpamx.splitter.gateway.room

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.gianpamx.splitter.gateway.room.model.ExpenseDBModel
import io.github.gianpamx.splitter.gateway.room.model.PaymentDBModel
import io.github.gianpamx.splitter.gateway.room.model.PersonDBModel
import io.github.gianpamx.splitter.gateway.room.model.ReceiverDBModel

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
