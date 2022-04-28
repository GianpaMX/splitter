package io.github.gianpamx.splitbetter.domain.usecase

import io.github.gianpamx.splitbetter.domain.entity.Person
import io.github.gianpamx.splitbetter.domain.dao.PeopleDao

class SavePerson(private val peopleDao: PeopleDao) {
    suspend operator fun invoke(person: Person) {
        peopleDao.save(person)
    }
}
