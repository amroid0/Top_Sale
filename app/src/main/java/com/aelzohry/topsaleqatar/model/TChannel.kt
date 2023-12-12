package com.aelzohry.topsaleqatar.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TChannel(
    val _id: String,
    val partner: User?,
    val sender: User?,
    val ad: Ad?,
    var lastMessage: TMessage?
) : Parcelable {
    val createdAt: String?
        get() = lastMessage?.createdAt

    var seen: Boolean
        get() = true // lastMessage?.seen ?: true
        set(newValue) {
            lastMessage?.seen = newValue
        }
}