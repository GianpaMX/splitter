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
    fun provideCreateExpenseUseCase(persistenceGateway: PersistenceGateway): SaveExpenseUseCase =
            SaveExpenseUseCaseImpl(persistenceGateway)

    @Provides
    fun provideKeepOrDeleteExpenseUseCase(persistenceGateway: PersistenceGateway): KeepOrDeleteExpenseUseCase =
            KeepOrDeleteExpenseUseCaseImpl(persistenceGateway)

    @Provides
    fun provideObserveExpensesUseCase(persistenceGateway: PersistenceGateway): ObserveExpensesUseCase =
            ObserveExpensesUseCaseImpl(persistenceGateway)

    @Provides
    fun provideGetExpenseUseCase(persistenceGateway: PersistenceGateway): GetExpenseUseCase =
            GetExpenseUseCaseImpl(persistenceGateway)
}
