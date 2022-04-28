package io.github.gianpamx.splitbetter.domain.dao

import io.github.gianpamx.splitbetter.domain.entity.Person
import kotlinx.coroutines.flow.Flow

interface PeopleDao {
    fun findAll(): Flow<List<Person>>
    suspend fun save(newPerson: Person)
}
