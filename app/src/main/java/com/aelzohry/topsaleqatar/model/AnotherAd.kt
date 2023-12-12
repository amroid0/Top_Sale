package com.aelzohry.topsaleqatar.model

import android.content.Context
import android.os.Parcelable
import com.aelzohry.topsaleqatar.R
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AnotherAd(
    val _id: String,
    val title: String,
    val details: String?,
    val price: Double,
    val isFixed: Boolean,
    val isEnabled: Boolean,
    val isApproved: Boolean,
    var isLiked: Boolean,
    var isActive: Boolean?,
    @SerializedName("status") val _status: String,
    val rejectReason: LocalizedModel?,
    var isFavourite: Boolean,
    val canRepublish: Boolean,
    var commentsCount: Int,
    var viewsCount: Int,
    var likesCount: Int,
    val publishedAt: String,
    val numberOfRooms: Int?,
    val numberOfBathroom: Int?,
    val space:Int?,
    val location:Location_1?,
    val user: User?,
    val category: String?,
    val region: LocalStanderModel?,
    val carMake: StanderModel?,
    val carModel: StanderModel?,
    val carYear: String?,
    val km: String?,
    val type: LocalStanderModel?,
    val subcategory: LocalStanderModel?,
    val photos: ArrayList<Photo>
) : Parcelable, AdBanner(_id) {
    val imageUrl: String?
        get() = this.photos.firstOrNull()?.orgUrl

    val status: AdStatus_1
        get() = AdStatus_1.getBy(_status)
}

enum class AdStatus_1 {
    ACTIVE, NOTACTIVE, PENDINGAPPROVAL, REJECTED;

    fun getTitle(context: Context): String {
        return context.getString(
            when (this) {
                ACTIVE -> R.string.active
                NOTACTIVE -> R.string.not_active
                PENDINGAPPROVAL -> R.string.pending_approval
                REJECTED -> R.string.rejected
            }
        )
    }

    companion object {
        fun getBy(status: String): AdStatus_1 {
            return when (status) {
                "not_active" -> NOTACTIVE
                "pending_approval" -> PENDINGAPPROVAL
                "rejected" -> REJECTED
                else -> ACTIVE
            }
        }
    }
}

@Parcelize
data class Location_1 (

    var address:String,
    var type : String,
    var coordinates : List<Double>

):Parcelable