package io.github.gianpamx.splitter.gateway.room

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "Payer")
data class PayerDBModel(
        @PrimaryKey(autoGenerate = true)
        var id: Long = 0L,
        var name: String = "",
        var cents: Int = 0
)
