package com.yundom.view

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

fun View.showKeyboard() {
    (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).let { imm ->
        requestFocus()
        imm.showSoftInput(this, 0)
    }
}

fun View.hideKeyboard(): Boolean = try {
    (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow(windowToken, 0)
} catch (ignored: RuntimeException) {
    false
}
