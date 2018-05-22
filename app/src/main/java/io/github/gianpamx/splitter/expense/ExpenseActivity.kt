package io.github.gianpamx.splitter.expense

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import dagger.android.AndroidInjection
import io.github.gianpamx.splitter.R
import kotlinx.android.synthetic.main.expense_activity.*
import kotlinx.coroutines.experimental.launch
import javax.inject.Inject

class ExpenseActivity : AppCompatActivity(), PayerDialog.Listener {
    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var viewModel: ExpenseViewModel;

    private val payersAdapter = PayersAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, factory).get(ExpenseViewModel::class.java)

        setContentView(R.layout.expense_activity)
        setSupportActionBar(toolbar)

        tabLayout.addOnTabSelectedListener(FabAnimator(floatingActionButton))

        floatingActionButton.setOnClickListener { showPayerDialog(PayerModel()) }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = payersAdapter

        viewModel.payers.observe(this, Observer {
            it?.let { payersAdapter.replacePayers(it) }
        })

        payersAdapter.onPayerSelectedListener = { showPayerDialog(it) }
    }

    private fun showPayerDialog(payerModel: PayerModel) {
        val payerDialog = PayerDialog.newInstance(payerModel)
        payerDialog.show(supportFragmentManager, "DIALOG")
    }

    override fun onSave(payerModel: PayerModel) {
        launch {
            viewModel.save(payerModel)
        }
    }

    override fun onCancel() {
        // Do nothing
    }
}
