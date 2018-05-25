package io.github.gianpamx.splitter.expense

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ExpenseModel(
        var id: Long = 0,
        var title: String = "",
        var description: String = ""
) : Parcelable
