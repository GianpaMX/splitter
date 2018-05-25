package io.github.gianpamx.splitter.core

import dagger.Module
import dagger.Provides


@Module
class CoreModule {
    @Provides
    fun provideSavePayerUseCase(persistenceGateway: PersistenceGateway): SavePaymentUseCase =
            SavePaymentUseCaseImpl(persistenceGateway)

    @Provides
    fun provideObservePayersUseCase(persistenceGateway: PersistenceGateway): ObservePayersUseCase =
            ObservePayersUseCaseImpl(persistenceGateway)

    @Provides
    fun proviceCreateExpenseUseCase(persistenceGateway: PersistenceGateway): CreateExpenseUseCase =
            CreateExpenseUseCaseImpl(persistenceGateway)
}
