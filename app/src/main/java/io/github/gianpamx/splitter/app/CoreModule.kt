package io.github.gianpamx.splitter.app

import dagger.Module
import dagger.Provides
import io.github.gianpamx.splitter.core.GetExpense
import io.github.gianpamx.splitter.core.GetExpenseUseCase
import io.github.gianpamx.splitter.core.GetExpenseUseCaseImpl
import io.github.gianpamx.splitter.core.GetPayers
import io.github.gianpamx.splitter.core.GetReceivers
import io.github.gianpamx.splitter.core.KeepOrDeleteExpense
import io.github.gianpamx.splitter.core.KeepOrDeleteExpenseUseCase
import io.github.gianpamx.splitter.core.KeepOrDeleteExpenseUseCaseImpl
import io.github.gianpamx.splitter.core.ObserveExpenses
import io.github.gianpamx.splitter.core.ObservePayersUseCase
import io.github.gianpamx.splitter.core.ObservePayersUseCaseImpl
import io.github.gianpamx.splitter.core.ObserveReceiversUseCase
import io.github.gianpamx.splitter.core.ObserveReceiversUseCaseImpl
import io.github.gianpamx.splitter.core.PersistenceGateway
import io.github.gianpamx.splitter.core.SaveExpense
import io.github.gianpamx.splitter.core.SaveExpenseUseCase
import io.github.gianpamx.splitter.core.SaveExpenseUseCaseImpl
import io.github.gianpamx.splitter.core.SavePayment
import io.github.gianpamx.splitter.core.SavePaymentUseCase
import io.github.gianpamx.splitter.core.SavePaymentUseCaseImpl
import io.github.gianpamx.splitter.core.SaveReceiver
import io.github.gianpamx.splitter.core.SaveReceiverUseCase
import io.github.gianpamx.splitter.core.SaveReceiverUseCaseImpl
import io.github.gianpamx.splitter.core.SettleUpUseCase
import io.github.gianpamx.splitter.core.SettleUpUseCaseImpl

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
  fun provideGetExpense(persistenceGateway: PersistenceGateway) = GetExpense(persistenceGateway)

  @Provides
  fun provideGetPayers(persistenceGateway: PersistenceGateway) = GetPayers(persistenceGateway)

  @Provides
  fun provideSettleUpUseCase(persistenceGateway: PersistenceGateway): SettleUpUseCase =
    SettleUpUseCaseImpl(persistenceGateway)

  @Provides
  fun provideSavePayment(persistenceGateway: PersistenceGateway) = SavePayment(persistenceGateway)

  @Provides
  fun provideGetReceivers(persistenceGateway: PersistenceGateway) = GetReceivers(persistenceGateway)

  @Provides
  fun provideSaveReceiver(persistenceGateway: PersistenceGateway) = SaveReceiver(persistenceGateway)

  @Provides
  fun provideKeepOrDeleteExpense(persistenceGateway: PersistenceGateway) = KeepOrDeleteExpense(persistenceGateway)
}
