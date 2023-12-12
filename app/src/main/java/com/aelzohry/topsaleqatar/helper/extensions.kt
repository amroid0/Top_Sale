package com.aelzohry.topsaleqatar.helper

import android.content.Context
import android.content.res.ColorStateList
import android.text.format.DateUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.core.widget.ImageViewCompat
import com.aelzohry.topsaleqatar.R
import com.kaopiz.kprogresshud.KProgressHUD
import org.ocpsoft.prettytime.PrettyTime
import java.text.SimpleDateFormat
import java.util.*

fun Int.toBoolean() = this == 1

fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun View.setViewBackgroundWithoutResettingPadding(backgroundResId: Int) {
    val paddingBottom = this.paddingBottom
    val paddingLeft = this.paddingLeft
    val paddingRight = this.paddingRight
    val paddingTop = this.paddingTop
    this.setBackgroundResource(backgroundResId)
    this.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
}

fun ImageView.setTintColor(color: Int) {
    ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(color))
}

fun String.toEnglishDigits(): String {
    return this.replace('٠', '0')
        .replace('١', '1')
        .replace('٢', '2')
        .replace('٣', '3')
        .replace('٤', '4')
        .replace('٥', '5')
        .replace('٦', '6')
        .replace('٧', '7')
        .replace('٨', '8')
        .replace('٩', '9')
}

fun String.toFilePath(): String {
    return when {
        startsWith("http", true) -> this
        else -> (Constants.FILE_BASE_URL + this)
    }
}

fun String.toDate(): Date? {
    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
    formatter.timeZone = TimeZone.getTimeZone("UTC")
    return formatter.parse(this)
}

fun Date.ago(): String {
    val prettyTime = PrettyTime()
    return prettyTime.format(this)
    //return DateUtils.getRelativeTimeSpanString(time, Date().time, DateUtils.MINUTE_IN_MILLIS).toString()
}

fun Date.toDateOnlyString(): String {
    val formatter = SimpleDateFormat("MMM dd,yyy", Locale.ENGLISH)
    return formatter.format(this)
}

fun Date.isToday(): Boolean {
    return DateUtils.isToday(this.time)
}

fun Date.isYesterday(): Boolean {
    return DateUtils.isToday(this.time + DateUtils.DAY_IN_MILLIS)
}

fun Date.toTimeOnlyString(): String {
    val formatter = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
    return formatter.format(this)
}

fun showProgress(context: Context): KProgressHUD {
    return KProgressHUD.create(context)
        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
        .setLabel(context.getString(R.string.loading))
        .setCancellable(false)
        .setAnimationSpeed(2)
        .setDimAmount(0.5f)
        .show()
}