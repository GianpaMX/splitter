package io.github.gianpamx.splitter.data

import com.nhaarman.mockito_kotlin.mock
import dagger.Module
import dagger.Provides
import io.github.gianpamx.splitter.core.PersistenceGateway

@Module
class MockModule {
    @Provides
    fun providePersistenceGateway(): PersistenceGateway = mock()
}
