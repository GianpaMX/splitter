package io.github.gianpamx.splitbetter.home

import io.github.gianpamx.splitbetter.domain.entity.Balance
import io.github.gianpamx.splitbetter.domain.entity.Money
import io.github.gianpamx.splitbetter.domain.entity.money

data class HomeState(
    val total: Money = 0.money,
    val balance: List<Balance> = emptyList(),
)
