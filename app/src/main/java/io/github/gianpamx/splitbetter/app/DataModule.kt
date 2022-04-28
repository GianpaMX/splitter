package io.github.gianpamx.splitbetter.app

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.gianpamx.splitbetter.app.data.InMemoryExpenseDao
import io.github.gianpamx.splitbetter.app.data.InMemoryPeopleDao
import io.github.gianpamx.splitbetter.domain.dao.ExpenseDao
import io.github.gianpamx.splitbetter.domain.dao.PeopleDao
import io.github.gianpamx.splitbetter.domain.entity.Expense
import io.github.gianpamx.splitbetter.domain.entity.Money
import io.github.gianpamx.splitbetter.domain.entity.Payer
import io.github.gianpamx.splitbetter.domain.entity.Person
import io.github.gianpamx.splitbetter.domain.entity.Receiver
import javax.inject.Singleton

private val dummyPeople = listOf(
    Person("1", "Ted"),
    Person("2", "Robin"),
)

private val dummyExpenses: List<Expense> = listOf(
    Expense("1", "how", listOf(Payer(dummyPeople[0], Money(123))), listOf(Receiver(dummyPeople[1], true))),
    Expense("2", "I", listOf(Payer(dummyPeople[1], Money(456))), listOf(Receiver(dummyPeople[0], true)))
)

@Module
@InstallIn(SingletonComponent::class)
class DataModule {
    @Provides
    @Singleton
    fun providePeopleDao(): PeopleDao = InMemoryPeopleDao(dummyPeople)

    @Provides
    @Singleton
    fun provideExpenseDao(): ExpenseDao = InMemoryExpenseDao(dummyExpenses)
}
