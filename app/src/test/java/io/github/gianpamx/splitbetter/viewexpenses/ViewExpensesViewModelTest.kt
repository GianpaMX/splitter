package io.github.gianpamx.splitbetter.viewexpenses

import io.github.gianpamx.MainCoroutineRule
import io.github.gianpamx.splitbetter.app.AppAction
import kotlinx.coroutines.flow.MutableSharedFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ViewExpensesViewModelTest {
    private val actionsFlow = MutableSharedFlow<AppAction>()

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    lateinit var viewModel: ViewExpensesViewModel

    @Before
    fun setUp() {
        viewModel = ViewExpensesViewModel(TODO(), actionsFlow)
    }

    @Test
    fun dummy() {

    }
}
