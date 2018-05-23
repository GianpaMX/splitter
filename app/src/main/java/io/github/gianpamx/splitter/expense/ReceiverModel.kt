package io.github.gianpamx.splitter.expense

data class ReceiverModel(
        var id: Long = 0L,
        var name: String = "",
        val isChecked: Boolean = false
)
