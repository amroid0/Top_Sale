package com.aelzohry.topsaleqatar.utils.extenions

import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.*

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