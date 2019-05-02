package io.github.gianpamx.splitter.expense

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import dagger.android.AndroidInjection
import io.github.gianpamx.splitter.R
import io.github.gianpamx.splitter.expense.model.PayerModel
import io.github.gianpamx.splitter.expense.model.ReceiverModel
import kotlinx.android.synthetic.main.expense_activity.*
import java.text.NumberFormat
import javax.inject.Inject

private const val SELECTED_TAB = "SELECTED_TAB"

private const val PAYERS_TAB = 0

private const val EXPENSE = "EXPENSE"

class ExpenseActivity : AppCompatActivity(), PayerDialog.Listener, ReceiverDialog.Listener {
    @Inject
    lateinit var factory: ViewModelProvider.Factory

    @Inject
    lateinit var currencyFormat: NumberFormat

    private lateinit var viewModel: ExpenseViewModel

    private var expenseId = 0L

    private var selectedTab: Int = PAYERS_TAB

    private lateinit var payersAdapter: PayersAdapter

    private val receiversAdapter = ReceiversAdapter()

    companion object {
        fun newIntent(expenseId: Long, context: Context) = Intent(context, ExpenseActivity::class.java).apply {
            putExtra(EXPENSE, expenseId)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, factory).get(ExpenseViewModel::class.java)
        payersAdapter = PayersAdapter(currencyFormat)

        setContentView(R.layout.expense_activity)
        setSupportActionBar(toolbar)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

        recyclerView.layoutManager = LinearLayoutManager(this)

        expenseId = intent.getLongExtra(EXPENSE, 0L)

        viewModel.loadExpense(expenseId)

        viewModel.expense.observe(this, Observer {
            it?.let { titleEditText.setText(it.title) }
        })

        if (savedInstanceState != null) {
            selectedTab = savedInstanceState.getInt(SELECTED_TAB, PAYERS_TAB)
            tabLayout.getTabAt(selectedTab)?.select()
        }
        onTabSelected(selectedTab)

        tabLayout.addOnTabSelectedListener(FabAnimator(floatingActionButton))
        tabLayout.addOnTabSelectedListener(onTabSelectedListener)

        floatingActionButton.setOnClickListener {
            if (selectedTab == PAYERS_TAB) {
                showPayerDialog(PayerModel())
            } else {
                showReceiverDialog(ReceiverModel(isChecked = true))
            }
        }

        titleEditText.addTextChangedListener(expenseTitleWatcher)

        viewModel.total.observe(this, Observer {
            it?.let { totalTextView.text = currencyFormat.format(it) }
        })

        viewModel.payers.observe(this, Observer {
            it?.let { payersAdapter.replacePayers(it) }
        })
        viewModel.receivers.observe(this, Observer {
            it?.let { receiversAdapter.replaceReceivers(it) }
        })

        payersAdapter.onPayerSelectedListener = ::showPayerDialog
        receiversAdapter.onCheckedChangeListener = ::onSave
        receiversAdapter.onLongClickListener = ::showReceiverDialog
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        outState?.putInt(SELECTED_TAB, selectedTab)
    }

    private val onTabSelectedListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabReselected(tab: TabLayout.Tab?) {
            // Do nothing
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
            // Do nothing
        }

        override fun onTabSelected(tab: TabLayout.Tab?) {
            tab?.apply { onTabSelected(position) }
        }
    }

    private val expenseTitleWatcher = object : TextWatcher {
        override fun afterTextChanged(title: Editable?) {
            viewModel.save(title.toString(), expenseId)
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // Do nothing
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // Do nothing
        }
    }

    private fun onTabSelected(position: Int) {
        selectedTab = position
        when (position) {
            0 -> recyclerView.adapter = payersAdapter
            1 -> recyclerView.adapter = receiversAdapter
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?) = when (item?.itemId) {
        android.R.id.home -> exitExpense { finish() }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        exitExpense { super.onBackPressed() }
    }

    private fun exitExpense(exitFunction: (() -> Unit)? = null): Boolean {
        viewModel.exitExpense(expenseId)
        exitFunction?.invoke()
        return true
    }

    private fun showPayerDialog(payerModel: PayerModel) {
        PayerDialog.newInstance(payerModel).show(supportFragmentManager, "DIALOG")
    }

    private fun showReceiverDialog(receiverModel: ReceiverModel) {
        ReceiverDialog.newInstance(receiverModel).show(supportFragmentManager, "DIALOG")
    }

    override fun onSave(payerModel: PayerModel) {
        viewModel.save(payerModel, expenseId)
    }

    override fun onSave(receiverModel: ReceiverModel) {
        viewModel.save(receiverModel, expenseId)
    }

    override fun onCancel() {
        // Do nothing
    }
}
