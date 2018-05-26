package io.github.gianpamx.splitter.gateway.room.view

import android.arch.persistence.room.ColumnInfo

data class PayerDBView(
        @ColumnInfo(name = "id")
        var personId: Long = 0,
        var name: String = "",
        var cents: Int = 0
)
