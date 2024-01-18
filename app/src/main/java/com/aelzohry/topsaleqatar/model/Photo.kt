package com.aelzohry.topsaleqatar.model

import android.os.Parcelable
import com.aelzohry.topsaleqatar.helper.toFilePath
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Photo(
    val _id: String?="",
    val org: String?="",
    val thumb: String?="",
    val thumb600: String? = "",
    var isDefault:Boolean = false
) : Parcelable {
    val orgUrl: String
    get() = org?.toFilePath() ?:{""}.toString()

    val thumbUrl: String
        get() = thumb?.toFilePath() ?: {""}.toString()

    val mediumThumbUrl: String
        get() = thumb600 ?: ""
}