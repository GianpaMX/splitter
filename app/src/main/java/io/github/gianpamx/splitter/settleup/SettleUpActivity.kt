package io.github.gianpamx.splitter.settleup

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import dagger.android.AndroidInjection
import io.github.gianpamx.splitter.R
import kotlinx.android.synthetic.main.settle_up_activity.*
import java.text.NumberFormat
import javax.inject.Inject

class SettleUpActivity : AppCompatActivity() {
    @Inject
    lateinit var factory: ViewModelProvider.Factory

    @Inject
    lateinit var currencyFormat: NumberFormat

    private lateinit var viewModel: SettleUpViewModel

    private lateinit var cardsAdapter: CardsAdapter

    companion object {
        fun newIntent(context: Context) = Intent(context, SettleUpActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, factory).get(SettleUpViewModel::class.java)
        cardsAdapter = CardsAdapter(currencyFormat)

        setContentView(R.layout.settle_up_activity)
        setSupportActionBar(toolbar)

        cardsRecyclerView.layoutManager = LinearLayoutManager(this)
        cardsRecyclerView.adapter = cardsAdapter

        viewModel.total.observe(this, Observer {
            it?.let { supportActionBar?.subtitle = currencyFormat.format(it) }
        })

        viewModel.cards.observe(this, Observer {
            it?.let { cardsAdapter.replaceCards(it) }
        })
    }
}
