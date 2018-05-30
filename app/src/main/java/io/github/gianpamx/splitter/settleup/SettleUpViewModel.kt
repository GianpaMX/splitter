package io.github.gianpamx.splitter.settleup

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import javax.inject.Inject

class SettleUpViewModel @Inject constructor() : ViewModel() {
    val cards = MutableLiveData<List<CardModel>>()
    val total = MutableLiveData<Double>()

    init {
        val listOfCards = listOf(
                CardModel(
                        personId = 1,
                        personName = "Juan",
                        paid = 700.0,
                        spent = 300.0,
                        breakdownRows = listOf(
                                Pair(300.0, "Ana")
                        )
                ),
                CardModel(
                        personId = 2,
                        personName = "Pepe",
                        paid = 500.0,
                        spent = 300.0,
                        breakdownRows = listOf(
                                Pair(100.0, "Ana"),
                                Pair(100.0, "Ale")
                        )
                ),
                CardModel(
                        personId = 3,
                        personName = "Ana",
                        paid = 300.0,
                        spent = 700.0,
                        breakdownRows = listOf(
                                Pair(-300.0, "Juan"),
                                Pair(-100.0, "Pepe")
                        )
                ),
                CardModel(
                        personId = 4,
                        personName = "Ale",
                        paid = 0.0,
                        spent = 100.0,
                        breakdownRows = listOf(
                                Pair(-100.0, "Pepe")
                        )
                )
        )
        cards.postValue(listOfCards)
        total.postValue(listOfCards.sumByDouble { it.paid })
    }
}
