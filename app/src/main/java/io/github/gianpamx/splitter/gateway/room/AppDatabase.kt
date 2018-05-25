package io.github.gianpamx.splitter.gateway.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
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
