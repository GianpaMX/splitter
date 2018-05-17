package io.github.gianpamx.splitter.expense

import android.arch.core.executor.testing.InstantTaskExecutorRule
import io.github.gianpamx.splitter.core.SavePayerUseCase
import org.hamcrest.collection.IsIn
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ExpenseViewModelTest {
    @Rule
    @JvmField
    val instantExecutor = InstantTaskExecutorRule()

    @Mock
    private lateinit var savePayerUseCase: SavePayerUseCase

    private lateinit var expenseViewModel: ExpenseViewModel

    @Before
    fun setUp() {
        expenseViewModel = ExpenseViewModel(savePayerUseCase)
    }

    @Test
    fun empty() {
        val expectedPayer = PayerModel()

        expenseViewModel.save(expectedPayer)

        assertThat(expectedPayer, IsIn(expenseViewModel.payers.value))
    }
}
