package io.github.gianpamx.splitter.frameworks.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Person")
data class PersonDBModel(
        @PrimaryKey(autoGenerate = true)
        var id: Long = 0,
        var name: String = ""
)
