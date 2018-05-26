package io.github.gianpamx.splitter.expense

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ReceiverModel(
        var id: Long = 0L,
        var name: String = "",
        val isChecked: Boolean = false
) : Parcelable
