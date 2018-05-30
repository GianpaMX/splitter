package io.github.gianpamx.splitter.settleup

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.github.gianpamx.splitter.core.model.Card
import io.github.gianpamx.splitter.core.SettleUpUseCase
import io.github.gianpamx.splitter.core.toAmount
import io.github.gianpamx.splitter.settleup.model.CardModel
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import javax.inject.Inject

class SettleUpViewModel @Inject constructor(settleUpUseCase: SettleUpUseCase) : ViewModel() {
    val cards = MutableLiveData<List<CardModel>>()
    val total = MutableLiveData<Double>()

    init {
        launch(UI) {
            val listOfCards = async { settleUpUseCase.invoke().map { it.toCardModel() } }.await()

            cards.postValue(listOfCards)
            total.postValue(listOfCards.sumByDouble { it.paid })
        }
    }
}

private fun Card.toCardModel() = CardModel(
        personId = personId,
        personName = personName,
        paid = centsPaid.toAmount(),
        spent = centsSpent.toAmount(),
        breakdownRows = breakdownRows.map { it.toBreakdownRowModel() }
)

private fun Pair<Int, String>.toBreakdownRowModel() = Pair(first.toAmount(), second)
