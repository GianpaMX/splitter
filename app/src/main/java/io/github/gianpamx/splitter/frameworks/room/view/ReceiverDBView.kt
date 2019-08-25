package io.github.gianpamx.splitter.frameworks.room.view

import androidx.room.ColumnInfo
import io.github.gianpamx.splitter.frameworks.room.FALSE

data class ReceiverDBView(
        @ColumnInfo(name = "id")
        var personId: Long = 0,

        var name: String = "",

        var checked: Int = FALSE
)
