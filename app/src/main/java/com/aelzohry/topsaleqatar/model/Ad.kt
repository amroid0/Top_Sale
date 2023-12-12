package com.aelzohry.topsaleqatar.model

import android.content.Context
import android.os.Parcelable
import android.text.TextUtils
import com.aelzohry.topsaleqatar.R
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Ad(
    val _id: String,
    val title: String,
    val details: String?,
    val price: Double?,
    val isFixed: Boolean?,
    val isEnabled: Boolean?,
    val isApproved: Boolean?,
    var isLiked: Boolean?,
    var isActive: Boolean?,
    @SerializedName("status") val _status: String?,
    val rejectReason: LocalizedModel?,
    var isFavourite: Boolean?=false,
    val canRepublish: Boolean?=false,
    var commentsCount: Int?=0,
    var viewsCount: Int?=0,
    var likesCount: Int?=0,

    val publishedAt: String?="",
    val numberOfRooms: Int?=0,
    val numberOfBathroom: Int?=0,
    val space: Int?=0,
    val location: Location?,
    val user: User?,
    val category: Category?,
    val region: LocalStanderModel?,
    val carMake: StanderModel1?,
    val carModel: StanderModel1?,
    val carSubModel: StanderModel1?,
    val carYear: String?="",
    val gearBox: String?="",
    val engineCapacity: String?="",

    val fuelType: String?,
    val pushFatisType: String?,
    val color: String?,

    val km: String?,
    val type: LocalStanderModel?,
    val subcategory: LocalStanderModel?,
    val photos: ArrayList<Photo>?,
    val video: Photo?,

    val iaAllowComments: Boolean?

    ) : Parcelable, AdBanner(_id) {
    val imageUrl: String?
        get() = this.adPhoto.firstOrNull()?.orgUrl


    val thumbImageUrl: String?
        get() = this.adPhoto.firstOrNull()?.thumbUrl


    val adPhoto:ArrayList<Photo>
        get() = photos ?: arrayListOf()

    val status: AdStatus
        get() = AdStatus.getBy(_status.toString())

    val isCarAd: Boolean
//        get() = category?.categoryClass == CategoryClass.CARS
        get() = !TextUtils.isEmpty(km)




}

enum class AdStatus {
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
        fun getBy(status: String): AdStatus {
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
data class Location(

    var address: String,
    var type: String,
    var coordinates: List<Double>

) : Parcelable