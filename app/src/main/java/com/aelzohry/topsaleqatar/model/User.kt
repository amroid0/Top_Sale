package com.aelzohry.topsaleqatar.model

import android.os.Parcelable
import com.aelzohry.topsaleqatar.helper.toFilePath
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val _id: String,
    @SerializedName("name") val _name: String?,
    val mobile: String,
    var isFollowing: Boolean,
    var isChecked: Boolean,
    val email: String?,
    val bio: String?,
    val profilePhoto: String?,
    val stats: UserStats?,
    val location: Address?
) : Parcelable {
    val avatarUrl: String?
        get() = this.profilePhoto?.toFilePath()

    val toDict: Map<String, String?>
        get() {
            val result = mutableMapOf(
                "_id" to _id,
                "name" to _name,
                "mobile" to mobile
            )
            email?.let { result.put("email", it) }
            profilePhoto?.let { result.put("profilePhoto", it) }
            return result

        }
}

@Parcelize
data class UserStats(
    val adsCount: Int,
    val followingsCount: Int,
    val followersCount: Int,
    val favAdsCount: Int,
    val blocksCount: Int
) : Parcelable

@Parcelize
data class Address(val type: String, val address: String, val coordinates: List<Double>) :
    Parcelable