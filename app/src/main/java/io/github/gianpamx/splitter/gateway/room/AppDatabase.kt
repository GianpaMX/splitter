package io.github.gianpamx.splitter.gateway.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import io.github.gianpamx.splitter.gateway.room.DatabaseDao
import io.github.gianpamx.splitter.gateway.room.PayerDBModel

@Database(
        entities = [
            PayerDBModel::class
        ],
        version = 1,
        exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dao(): DatabaseDao
}
