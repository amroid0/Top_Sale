package com.aelzohry.topsaleqatar.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Not(
    val _id: String,
    val createdAt: String,
    val body: LocalizedModel,
    var seen: Boolean,
    @SerializedName("user") val userId: String?,
    val objectUser: User?,
    val objectCategory: Category?,
    val objectComment: Comment?,
    val objectAd: Ad?
): Parcelable {
    val imageUrl: String?
        get() =
            objectUser?.avatarUrl ?:
            objectCategory?.imageUrl ?:
            objectComment?.user?.avatarUrl ?:
            objectAd?.imageUrl
}

data class TNewNotification(
    @SerializedName("new_notification") val newNotification: Not
)