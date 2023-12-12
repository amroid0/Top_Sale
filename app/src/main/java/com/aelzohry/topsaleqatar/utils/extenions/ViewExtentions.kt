package com.aelzohry.topsaleqatar.utils.extenions

import android.content.Context
import android.graphics.Typeface
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.android.material.textfield.TextInputLayout

fun View.setVisible(boolean: Boolean) {
    this.visibility = if (boolean) View.VISIBLE else View.GONE

    fun View.showKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

}
