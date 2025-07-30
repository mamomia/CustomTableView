package com.mushi.customtableview.util

import android.text.Editable
import android.text.TextWatcher

abstract class CustomTextWatcher(var column: Int, var row: Int) : TextWatcher {
    private var oldValue = ""
    private var newValue = ""

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        oldValue = s.toString()
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
    }

    override fun afterTextChanged(s: Editable) {
        newValue = s.toString()
        textChanged(oldValue, newValue)
    }

    open fun textChanged(oldValue: String?, newValue: String?) {
    }
}