package io.github.gianpamx.splitter.gateway.room

import dagger.Module
import dagger.Provides
import io.github.gianpamx.splitter.core.PersistenceGateway

@Module
class RoomModule {
    @Provides
    fun providePersistenceGateway(): PersistenceGateway = RoomPersistence()
}