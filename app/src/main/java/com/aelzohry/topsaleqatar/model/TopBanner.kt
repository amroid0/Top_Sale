package com.aelzohry.topsaleqatar.model

import com.aelzohry.topsaleqatar.helper.*
import com.google.gson.annotations.SerializedName

data class TopBanner(
    val _id: String,
    val title: LocalizedModel?,
    val photo: String,
    val url: String?,
    val ad: Ad?,
    val category: Category?,
    @SerializedName("type") val _type: String
) {
    val photoUrl: String
        get() = this.photo.toFilePath()

    val type: TopBannerType
        get() = TopBannerType.getBy(_type)
}

enum class TopBannerType {
    URL, CATEGORY, AD;

    companion object {
        fun getBy(type: String): TopBannerType {
            return when (type) {
                "category" -> TopBannerType.CATEGORY
                "ad" -> TopBannerType.AD
                else -> TopBannerType.URL
            }
        }
    }
}