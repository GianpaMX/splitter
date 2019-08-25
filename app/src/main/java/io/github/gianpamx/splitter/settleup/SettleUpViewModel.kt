package io.github.gianpamx.splitter.settleup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.gianpamx.splitter.core.SettleUpUseCase
import io.github.gianpamx.splitter.core.entity.Card
import io.github.gianpamx.splitter.core.toAmount
import io.github.gianpamx.splitter.settleup.model.CardModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettleUpViewModel @Inject constructor(settleUpUseCase: SettleUpUseCase) : ViewModel() {
    val cards = MutableLiveData<List<CardModel>>()
    val total = MutableLiveData<Double>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val listOfCards = settleUpUseCase.invoke().map { it.toCardModel() }

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
