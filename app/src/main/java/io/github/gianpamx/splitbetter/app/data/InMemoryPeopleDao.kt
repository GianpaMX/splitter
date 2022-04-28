package io.github.gianpamx.splitbetter.app.data

import io.github.gianpamx.splitbetter.domain.dao.PeopleDao
import io.github.gianpamx.splitbetter.domain.entity.Person
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged

class InMemoryPeopleDao(initialValue: List<Person>) : PeopleDao {

    private var people: List<Person>

    private val peopleFlow: MutableSharedFlow<List<Person>> = MutableSharedFlow(replay = 1)

    override fun findAll(): Flow<List<Person>> = peopleFlow.distinctUntilChanged()

    init {
        people = initialValue
        peopleFlow.tryEmit(people)
    }

    override suspend fun save(newPerson: Person) {
        val alreadyExists = people.find { it.id == newPerson.id } != null

        people = if (alreadyExists) {
            people.map { existingPerson ->
                if (existingPerson.id == newPerson.id) newPerson else existingPerson
            }
        } else {
            people + newPerson
        }

        peopleFlow.emit(people)
    }
}
