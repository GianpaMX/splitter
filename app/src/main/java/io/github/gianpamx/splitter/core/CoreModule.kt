package io.github.gianpamx.splitter.core

import dagger.Module
import dagger.Provides


@Module
class CoreModule {
    @Provides
    fun provideSavePayerUseCase(persistenceGateway: PersistenceGateway): SavePayerUseCase = SavePayerUseCaseImpl(persistenceGateway)
}
