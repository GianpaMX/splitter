package io.github.gianpamx.splitter.expense

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import dagger.android.AndroidInjection
import io.github.gianpamx.splitter.R
import kotlinx.android.synthetic.main.expense_activity.*
import kotlinx.coroutines.experimental.launch
import javax.inject.Inject

private const val SELECTED_TAB = "SELECTED_TAB"

private const val PAYERS_TAB = 0

private const val EXPENSE = "EXPENSE"

class ExpenseActivity : AppCompatActivity(), PayerDialog.Listener, ReceiverDialog.Listener {
    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var viewModel: ExpenseViewModel

    private lateinit var expense: ExpenseModel

    private var selectedTab: Int = PAYERS_TAB

    private val payersAdapter = PayersAdapter()

    private val receiversAdapter = ReceiversAdapter()

    companion object {
        fun newIntent(expense: ExpenseModel, context: Context): Intent {
            val intent = Intent(context, ExpenseActivity::class.java)
            intent.putExtra(EXPENSE, expense)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, factory).get(ExpenseViewModel::class.java)

        setContentView(R.layout.expense_activity)
        setSupportActionBar(toolbar)

        recyclerView.layoutManager = LinearLayoutManager(this)

        expense = intent.getParcelableExtra(EXPENSE)
        viewModel.observePayers(expense.id)

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

        viewModel.payers.observe(this, Observer {
            it?.let { payersAdapter.replacePayers(it) }
        })

        viewModel.receivers.observe(this, Observer {
            it?.let { receiversAdapter.replaceReceivers(it) }
        })

        payersAdapter.onPayerSelectedListener = { showPayerDialog(it) }
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

    private fun onTabSelected(position: Int) {
        selectedTab = position
        when (position) {
            0 -> recyclerView.adapter = payersAdapter
            1 -> recyclerView.adapter = receiversAdapter
        }
    }

    private fun showPayerDialog(payerModel: PayerModel) {
        val payerDialog = PayerDialog.newInstance(payerModel)
        payerDialog.show(supportFragmentManager, "DIALOG")
    }

    private fun showReceiverDialog(receiverModel: ReceiverModel) {
        val payerDialog = ReceiverDialog.newInstance(receiverModel)
        payerDialog.show(supportFragmentManager, "DIALOG")
    }

    override fun onSave(payerModel: PayerModel) {
        launch {
            viewModel.save(payerModel, expense.id)
        }
    }

    override fun onSave(receiverModel: ReceiverModel) {
        launch {
            viewModel.save(receiverModel, expense.id)
        }
    }

    override fun onCancel() {
        // Do nothing
    }
}
