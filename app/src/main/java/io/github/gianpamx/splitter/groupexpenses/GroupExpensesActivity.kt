package io.github.gianpamx.splitter.groupexpenses

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.android.AndroidInjection
import io.github.gianpamx.splitter.R
import io.github.gianpamx.splitter.expense.ExpenseActivity
import io.github.gianpamx.splitter.settleup.SettleUpActivity
import kotlinx.android.synthetic.main.group_expenses_activity.*
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

        viewModel.fabVisivility.observe(this, Observer {
            if (it) {
                addExpenseFAB.show()
            } else {
                addExpenseFAB.hide()
            }
        })

        viewModel.newExpenseId.observe(this, Observer {
            startActivity(ExpenseActivity.newIntent(it, this))
        })

        addExpenseFAB.setOnClickListener {
            viewModel.createExpense()
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
