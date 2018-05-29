package io.github.gianpamx.splitter.data

import com.nhaarman.mockito_kotlin.mock
import dagger.Module
import dagger.Provides
import io.github.gianpamx.splitter.core.PersistenceGateway
import javax.inject.Singleton

@Module
class MockModule {
    @Provides
    @Singleton
    fun providePersistenceGateway(): PersistenceGateway = mock()
}
