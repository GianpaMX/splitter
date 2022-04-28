package io.github.gianpamx.splitbetter.domain.usecase

import io.github.gianpamx.splitbetter.domain.entity.Person
import io.github.gianpamx.splitbetter.domain.dao.PeopleDao
import kotlinx.coroutines.flow.Flow

class GetPeople(private val peopleDao: PeopleDao) {
    operator fun invoke(): Flow<List<Person>> = peopleDao.findAll()
}
