package io.github.gianpamx.splitter.core

import dagger.Module
import dagger.Provides


@Module
class CoreModule {
    @Provides
    fun provideSavePayerUseCase(persistenceGateway: PersistenceGateway): SavePaymentUseCase =
            SavePaymentUseCaseImpl(persistenceGateway)

    @Provides
    fun provideSaveReceiverUseCase(persistenceGateway: PersistenceGateway): SaveReceiverUseCase =
            SaveReceiverUseCaseImpl(persistenceGateway)

    @Provides
    fun provideObservePayersUseCase(persistenceGateway: PersistenceGateway): ObservePayersUseCase =
            ObservePayersUseCaseImpl(persistenceGateway)

    @Provides
    fun provideObserveReceiversUseCase(persistenceGateway: PersistenceGateway): ObserveReceiversUseCase =
            ObserveReceiversUseCaseImpl(persistenceGateway)

    @Provides
    fun proviceCreateExpenseUseCase(persistenceGateway: PersistenceGateway): CreateExpenseUseCase =
            CreateExpenseUseCaseImpl(persistenceGateway)

    @Provides
    fun proviceKeepOrDeleteExpenseUseCase(persistenceGateway: PersistenceGateway): KeepOrDeleteExpenseUseCase =
            KeepOrDeleteExpenseUseCaseImpl(persistenceGateway)
}
