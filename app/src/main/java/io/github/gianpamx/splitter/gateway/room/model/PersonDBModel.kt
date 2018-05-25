package io.github.gianpamx.splitter.gateway.room.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "Person")
data class PersonDBModel(
        @PrimaryKey(autoGenerate = true)
        var id: Long = 0,
        var name: String = ""
)
