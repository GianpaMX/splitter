package io.github.gianpamx.splitter.expense

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import dagger.android.AndroidInjection
import io.github.gianpamx.splitter.R
import kotlinx.android.synthetic.main.expense_activity.*
import javax.inject.Inject

class ExpenseActivity : AppCompatActivity(), PayerDialog.Listener {
    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var viewModel: ExpenseViewModel;

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, factory).get(ExpenseViewModel::class.java)

        setContentView(R.layout.expense_activity)
        setSupportActionBar(toolbar)

        tabLayout.addOnTabSelectedListener(FabAnimator(floatingActionButton))

        floatingActionButton.setOnClickListener {
            val payerDialog = PayerDialog()
            payerDialog.show(supportFragmentManager, "DIALOG")
        }
    }

    override fun onSave(payerModel: PayerModel) {
        viewModel.save(payerModel)
    }

    override fun onCancel() {

    }
}
