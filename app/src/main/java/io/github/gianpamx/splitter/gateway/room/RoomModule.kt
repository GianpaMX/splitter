package io.github.gianpamx.splitter.gateway.room

import androidx.room.Room
import android.content.Context
import dagger.Module
import dagger.Provides
import io.github.gianpamx.splitter.core.PersistenceGateway
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
