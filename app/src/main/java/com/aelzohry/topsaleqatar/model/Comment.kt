package com.aelzohry.topsaleqatar.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Comment(
    val _id: String,
    var text: String,
    val createdAt: String,
    val user: User?,
    val isAdOwner: Boolean,
    val isCommentOwner: Boolean,
    @SerializedName("ad") val adId: String?
) : Parcelable