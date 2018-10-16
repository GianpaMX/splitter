package io.github.gianpamx.splitter.data

import androidx.room.Room
import android.content.Context
import dagger.Module
import dagger.Provides
import io.github.gianpamx.splitter.core.PersistenceGateway
import io.github.gianpamx.splitter.gateway.room.AppDatabase
import io.github.gianpamx.splitter.gateway.room.DatabaseDao
import io.github.gianpamx.splitter.gateway.room.RoomPersistence
import javax.inject.Singleton


@Module
class MockModule {

    @Provides
    @Singleton
    fun provideDatabase(context: Context) =
            Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).allowMainThreadQueries().build()

    @Provides
    @Singleton
    fun providesDatabaseDao(database: AppDatabase) =
            database.dao()

    @Provides
    @Singleton
    fun providePersistenceGateway(databaseDao: DatabaseDao): PersistenceGateway =
            RoomPersistence(databaseDao)
}
