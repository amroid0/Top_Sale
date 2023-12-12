package com.aelzohry.topsaleqatar.repository.remote.responses

import android.os.Parcel
import android.os.Parcelable
import com.aelzohry.topsaleqatar.model.Category
import com.aelzohry.topsaleqatar.model.LocalizedModel
import com.google.gson.annotations.SerializedName

data class CategoryData(

    @SerializedName("title") val title: LocalizedModel?,
    @SerializedName("order") val order: Int,
    @SerializedName("_id") val _id: String?,
    @SerializedName("categories") var categories: ArrayList<Category>
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(LocalizedModel::class.java.classLoader),
        parcel.readInt(),
        parcel.readString(),
        arrayListOf<Category>().apply {
            parcel.readArrayList(Category::class.java.classLoader)
        }
    )


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(title, flags)
        parcel.writeInt(order)
        parcel.writeString(_id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CategoryData> {
        override fun createFromParcel(parcel: Parcel): CategoryData {
            return CategoryData(parcel)
        }

        override fun newArray(size: Int): Array<CategoryData?> {
            return arrayOfNulls(size)
        }


    }
}