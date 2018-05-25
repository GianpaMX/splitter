package io.github.gianpamx.splitter.groupexpenses

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import dagger.android.AndroidInjection
import io.github.gianpamx.splitter.R
import io.github.gianpamx.splitter.app.Android
import io.github.gianpamx.splitter.expense.ExpenseActivity
import kotlinx.android.synthetic.main.group_expenses_activity.*
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import javax.inject.Inject

class GroupExpensesActivity : AppCompatActivity() {
    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var viewModel: GroupExpensesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, factory).get(GroupExpensesViewModel::class.java)

        setContentView(R.layout.group_expenses_activity)

        floatingActionButton.setOnClickListener {
            floatingActionButton.hide()
            launch(Android) {
                val expense = async { viewModel.createExpense() }.await()
                floatingActionButton.show()
                startActivity(ExpenseActivity.newIntent(expense, this@GroupExpensesActivity))
            }
        }
    }
}
