package io.github.gianpamx.splitter.expense

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

fun View.hideKeyBoard() {
    val inputMethodManager = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(this.windowToken, 0)
}