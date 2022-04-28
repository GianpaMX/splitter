package io.github.gianpamx.splitbetter.domain

import io.github.gianpamx.splitbetter.domain.usecase.GetPeople
import org.junit.Test

class GetPeopleTest {
    @Test
    fun empty() {
        val getPeople = GetPeople()

        getPeople()
    }
}
