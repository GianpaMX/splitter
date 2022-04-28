package io.github.gianpamx.splitbetter.app

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.gianpamx.splitbetter.domain.dao.ExpenseDao
import io.github.gianpamx.splitbetter.domain.dao.PeopleDao
import io.github.gianpamx.splitbetter.domain.gateway.UniqueIdGateway
import io.github.gianpamx.splitbetter.domain.usecase.GetExpense
import io.github.gianpamx.splitbetter.domain.usecase.GetExpenses
import io.github.gianpamx.splitbetter.domain.usecase.GetPeople
import io.github.gianpamx.splitbetter.domain.usecase.NewPerson
import io.github.gianpamx.splitbetter.domain.usecase.SaveExpense
import io.github.gianpamx.splitbetter.domain.usecase.SavePerson
import io.github.gianpamx.splitbetter.domain.usecase.SettleUp
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DomainModule {
    @Provides
    @Singleton
    fun provideGetPeople(peopleDao: PeopleDao) = GetPeople(peopleDao)

    @Provides
    @Singleton
    fun provideNewPerson(uniqueIdGateway: UniqueIdGateway) = NewPerson(uniqueIdGateway)

    @Provides
    @Singleton
    fun provideSavePerson(peopleDao: PeopleDao) = SavePerson(peopleDao)

    @Provides
    @Singleton
    fun provideSettleUp(getPeople: GetPeople, expenseDao: ExpenseDao) = SettleUp(getPeople, expenseDao)

    @Provides
    @Singleton
    fun provideGetExpense(expenseDao: ExpenseDao) = GetExpense(expenseDao)

    @Provides
    @Singleton
    fun provideGetExpenses(expenseDao: ExpenseDao) = GetExpenses(expenseDao)

    @Provides
    @Singleton
    fun provideSaveExpense(uniqueIdGateway: UniqueIdGateway, expenseDao: ExpenseDao) = SaveExpense(uniqueIdGateway, expenseDao)
}
