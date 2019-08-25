package io.github.gianpamx.splitter.app

import androidx.room.Room
import android.content.Context
import dagger.Module
import dagger.Provides
import io.github.gianpamx.splitter.core.PersistenceGateway
import io.github.gianpamx.splitter.frameworks.room.AppDatabase
import io.github.gianpamx.splitter.frameworks.room.DatabaseDao
import io.github.gianpamx.splitter.frameworks.room.RoomPersistence
import javax.inject.Singleton

@Module
class RoomModule {
    @Provides
    @Singleton
    fun providesAppDatabase(context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, "splitter")
                    .fallbackToDestructiveMigration()
                    .build()

    @Provides
    @Singleton
    fun providesDatabaseDao(database: AppDatabase) = database.dao()

    @Provides
    @Singleton
    fun providePersistenceGateway(databaseDao: DatabaseDao): PersistenceGateway =
            RoomPersistence(databaseDao)
}
