package io.github.gianpamx.splitter.expense

import android.arch.core.executor.testing.InstantTaskExecutorRule
import io.github.gianpamx.splitter.core.Payer
import io.github.gianpamx.splitter.core.SavePayerUseCase
import kotlinx.coroutines.experimental.runBlocking
import org.hamcrest.collection.IsIn
import org.hamcrest.core.IsInstanceOf
import org.junit.Assert.assertThat
import org.junit.Rule
import org.junit.Test

class ExpenseViewModelTest {
    @Rule
    @JvmField
    val instantExecutor = InstantTaskExecutorRule()

    @Test
    fun saveNewPayer() {
        val expectedPayer = PayerModel(name = "ANY_NAME", amount = "$ 123.45")
        val mock = object : SavePayerUseCase {
            override suspend fun invoke(player: Payer) = listOf(Payer(name = "ANY_NAME", cents = 12345))
        }
        val sut = ExpenseViewModel(mock)

        runBlocking {
            sut.save(expectedPayer)
        }

        assertThat(expectedPayer, IsIn(sut.payers.value!!))
    }

    @Test
    fun errorSavingAPayer() {
        val mock = object : SavePayerUseCase {
            override suspend fun invoke(player: Payer) = throw Exception("ANY_EXCEPTION")
        }
        val sut = ExpenseViewModel(mock)

        runBlocking {
            sut.save(PayerModel())
        }

        assertThat(sut.error.value, IsInstanceOf(Exception::class.java))
    }
}
