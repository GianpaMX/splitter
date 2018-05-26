package io.github.gianpamx.splitter.gateway.room.view

import android.arch.persistence.room.ColumnInfo
import io.github.gianpamx.splitter.gateway.room.FALSE

data class ReceiverDBView(
        @ColumnInfo(name = "id")
        var personId: Long = 0,

        var name: String = "",

        var checked: Int = FALSE
)
