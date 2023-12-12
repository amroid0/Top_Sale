package com.aelzohry.topsaleqatar.utils.extenions

import android.app.Dialog
import android.os.Handler
import android.text.TextUtils
import android.util.Patterns
import android.view.animation.AnimationUtils
import android.widget.EditText
import androidx.annotation.StringRes
import com.google.android.material.textfield.TextInputLayout
import com.aelzohry.topsaleqatar.R
import com.aelzohry.topsaleqatar.helper.Constants

fun TextInputLayout.value(isRemoveSpace: Boolean = false): String {
    if (isRemoveSpace)
        return editText?.text?.replace("\\s".toRegex(), "") ?: ""
    return editText?.text.toString()
}

fun TextInputLayout.setError(@StringRes errorRes: Int, editText: EditText? = getEditText()) {

    error = context.getString(errorRes)
    editText?.requestFocus()
    startAnimation(AnimationUtils.loadAnimation(context, R.anim.shake))
    Handler().postDelayed({
        error = null
    }, 2000)
}


fun TextInputLayout.isValidText(): Boolean {
    var value = editText?.text?.toString()
    if (TextUtils.isEmpty(value)) {
        setError(R.string.requiredField, editText!!)
        return false
    }

    return true
}

fun String.isValidText(): Int {
    if (TextUtils.isEmpty(this)) {
        return R.string.requiredField
    }
    return 0
}

fun TextInputLayout.isValidEmail(): Boolean {

    if (!isValidText())
        return false

    if (!Patterns.EMAIL_ADDRESS.matcher(value()).matches()) {
        setError(R.string.invalidEmail, editText!!)
        return false
    }

    return true
}

fun String.isValidEmail(): Int {

    if (isValidText() != 0)
        return isValidText()

    if (!Patterns.EMAIL_ADDRESS.matcher(this).matches()) {
        return R.string.invalidEmail
    }

    return 0
}

fun TextInputLayout.isValidPhone(): Boolean {

    if (!isValidText())
        return false

    if (!Patterns.PHONE.matcher(value()).matches()) {
        setError(R.string.invalidPhone)
        return false
    }


    return true

}

fun String.isValidPhone(): Int {

    if (isValidText() != 0)
        return isValidText()

    if (!Patterns.PHONE.matcher(this).matches())
        return R.string.invalidPhone

    return 0

}

fun TextInputLayout.isValidPassword(): Boolean {

    if (!isValidText())
        return false

    if (value().length <= Constants.PASS_LENGTH) {
        setError(R.string.shortPass)
        return false
    }

    return true
}

fun String.isValidPassword(): Int {

    if (isValidText() != 0)
        return isValidText()

    if (this.length <= Constants.PASS_LENGTH)
        return R.string.shortPass

    return 0
}

fun TextInputLayout.isMatchPassword(pass: TextInputLayout): Boolean {

    if (!isValidText())
        return false

    if (!TextUtils.equals(value(), pass.value())) {
        setError(R.string.noMatchPassword)
        return false
    }

    return true
}

fun String.isMatchPassword(pass: String): Int {

    if (isValidText() != 0)
        return isValidText()

    if (!TextUtils.equals(this, pass))
        return R.string.noMatchPassword
    return 0
}

fun TextInputLayout.isValidDigits(): Boolean {

    if (!isValidText())
        return false

    if (!value().matches("[0-9]+".toRegex())) {
        setError(R.string.invalidValue)
        return false
    }

    return true
}

fun TextInputLayout.isValidField(
    isValid: Boolean,
    dialog: Dialog?,
    @StringRes errorRes: Int
): Boolean {

    if (!isValid)
        context.snackBar(errorRes, R.string.select) { dialog?.show() }
    return isValid
}

