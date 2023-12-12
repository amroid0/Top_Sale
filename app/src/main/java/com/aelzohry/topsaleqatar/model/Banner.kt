package com.aelzohry.topsaleqatar.model

import com.aelzohry.topsaleqatar.helper.*

 class Banner(
    val _id: String,
    val photo: String,
    val url: String?,
    val ad: Ad?
): AdBanner(_id) {
    val photoUrl: String
        get() = this.photo.toFilePath()
}

open class AdBanner(val id:String)