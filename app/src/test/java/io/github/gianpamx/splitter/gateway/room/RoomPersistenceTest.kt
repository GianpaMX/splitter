package io.github.gianpamx.splitter.gateway.room

import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.github.gianpamx.splitter.core.Payer
import io.reactivex.internal.operators.flowable.FlowableJust
import org.hamcrest.collection.IsIn
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RoomPersistenceTest {

    @Mock
    private lateinit var databaseDao: DatabaseDao

    private lateinit var roomPersistence: RoomPersistence

    @Before
    fun setUp() {
        roomPersistence = RoomPersistence(databaseDao)
    }

    @Test
    fun create() {
        val expectedPayer = Payer(id = 1, name = "name", cents = 123)

        roomPersistence.create(expectedPayer)

        verify(databaseDao).insert(eq(PayerDBModel(id = 1, name = "name", cents = 123)))
    }

    @Test
    fun findAllPayers() {
        whenever(databaseDao.retriveAllPayers()).thenReturn(listOf(PayerDBModel(id = 1)))

        val allPayers = roomPersistence.findAllPayers()

        assertThat(Payer(id = 1), IsIn(allPayers))
    }

    @Test
    fun update() {
        val expectedPayer = Payer(id = 1, name = "name", cents = 123)

        roomPersistence.update(expectedPayer)

        verify(databaseDao).update(eq(PayerDBModel(id = 1, name = "name", cents = 123)))
    }

    @Test
    fun observePayers() {
        val observer = mock<(List<Payer>) -> Unit>()
        whenever(databaseDao.allPayers()).thenReturn(FlowableJust(listOf(PayerDBModel(id = 1))))

        roomPersistence.observePayers(observer)

        verify(observer).invoke(eq(listOf(Payer(id = 1))))
    }
}
