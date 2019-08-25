package io.github.gianpamx.splitter.frameworks.room.view

import androidx.room.ColumnInfo

data class PayerDBView(
        @ColumnInfo(name = "id")
        var personId: Long = 0,
        var name: String = "",
        var cents: Int = 0
)
