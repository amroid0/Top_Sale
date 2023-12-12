package com.aelzohry.topsaleqatar.utils.extenions

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes


fun Context.showToast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

fun Context.showToast(@StringRes msgRes: Int) = showToast(getString(msgRes))

