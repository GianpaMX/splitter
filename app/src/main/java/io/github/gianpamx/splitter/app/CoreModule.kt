package io.github.gianpamx.splitter.app

import dagger.Module
import dagger.Provides
import io.github.gianpamx.splitter.core.*


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
    fun provideCreateExpense(persistenceGateway: PersistenceGateway) =
            SaveExpense(persistenceGateway)

    @Provides
    fun provideKeepOrDeleteExpenseUseCase(persistenceGateway: PersistenceGateway): KeepOrDeleteExpenseUseCase =
            KeepOrDeleteExpenseUseCaseImpl(persistenceGateway)

    @Provides
    fun provideObserveExpensesUseCase(persistenceGateway: PersistenceGateway): ObserveExpenses =
            ObserveExpenses(persistenceGateway)

    @Provides
    fun provideGetExpenseUseCase(persistenceGateway: PersistenceGateway): GetExpenseUseCase =
            GetExpenseUseCaseImpl(persistenceGateway)

    @Provides
    fun provideSettleUpUseCase(persistenceGateway: PersistenceGateway): SettleUpUseCase =
            SettleUpUseCaseImpl(persistenceGateway)
}
