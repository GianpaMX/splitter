package io.github.gianpamx.splitbetter.domain.usecase

import io.github.gianpamx.splitbetter.domain.entity.Person
import io.github.gianpamx.splitbetter.domain.gateway.UniqueIdGateway

class NewPerson(private val uniqueIdGateway: UniqueIdGateway) {
    operator fun invoke(name: String = ""): Person = Person(id = uniqueIdGateway.getUniqueId(), name)
}
