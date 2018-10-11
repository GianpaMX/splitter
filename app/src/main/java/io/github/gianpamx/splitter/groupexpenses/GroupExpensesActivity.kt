package io.github.gianpamx.splitter.groupexpenses

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import dagger.android.AndroidInjection
import io.github.gianpamx.splitter.R
import io.github.gianpamx.splitter.expense.ExpenseActivity
import io.github.gianpamx.splitter.settleup.SettleUpActivity
import kotlinx.android.synthetic.main.group_expenses_activity.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import java.text.NumberFormat
import javax.inject.Inject

class GroupExpensesActivity : AppCompatActivity() {
    @Inject
    lateinit var factory: ViewModelProvider.Factory

    @Inject
    lateinit var currencyFormat: NumberFormat

    private lateinit var expensesAdapter: ExpensesAdapter

    private lateinit var viewModel: GroupExpensesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, factory).get(GroupExpensesViewModel::class.java)
        expensesAdapter = ExpensesAdapter(currencyFormat)
        expensesAdapter.onClickListener = {
            startActivity(ExpenseActivity.newIntent(it.id, this@GroupExpensesActivity))
        }

        setContentView(R.layout.group_expenses_activity)
        setSupportActionBar(toolbar)

        expenseRecyclerView.layoutManager = LinearLayoutManager(this)
        expenseRecyclerView.adapter = expensesAdapter

        viewModel.expenses.observe(this, Observer {
            it?.let { expensesAdapter.replaceExpenses(it) }
        })

        viewModel.total.observe(this, Observer {
            it?.let { supportActionBar?.subtitle = currencyFormat.format(it) }
        })

        addExpenseFAB.setOnClickListener {
            addExpenseFAB.hide()
            launch(UI) {
                val expenseId = async { viewModel.createExpense() }.await()
                addExpenseFAB.show()
                startActivity(ExpenseActivity.newIntent(expenseId, this@GroupExpensesActivity))
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.group_expenses_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?) = when (item?.itemId) {
        R.id.settleUpItem -> {
            startActivity(SettleUpActivity.newIntent(this))
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}
