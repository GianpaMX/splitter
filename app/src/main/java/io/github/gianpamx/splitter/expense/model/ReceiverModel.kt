package io.github.gianpamx.splitter.expense.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ReceiverModel(
        var id: Long = 0L,
        var name: String = "",
        var isChecked: Boolean = false
) : Parcelable
