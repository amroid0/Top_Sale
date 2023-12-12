package com.aelzohry.topsaleqatar.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CarModel(
    val _id: String,
    val title: String
): Parcelable