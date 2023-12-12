package com.aelzohry.topsaleqatar.model

import android.os.Parcelable
import com.aelzohry.topsaleqatar.helper.AppLanguage
import com.aelzohry.topsaleqatar.helper.Helper
import com.aelzohry.topsaleqatar.helper.toFilePath
import kotlinx.android.parcel.Parcelize
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

@Parcelize
data class Category(
    val _id: String,
    val title: LocalizedModel,
    val type: String?,
    val image: String?,
    val isLocationRequired: Boolean?,
    var isSelected: Boolean= false,
    val types: ArrayList<LocalStanderModel>?,
    val subcategories: ArrayList<LocalStanderModel>?
) : Parcelable {
    val imageUrl: String?
        get() = this.image?.toFilePath()

    val categoryClass: CategoryClass
        get() = CategoryClass.getByClass(type?.toLowerCase(Locale.ENGLISH) ?: "general")
}

enum class CategoryClass {
    GENERAL, PROPERTIES, CARS,LANDS;

    companion object {
        fun getByClass(type: String): CategoryClass {
            return when (type) {
                "cars" -> CARS
                "lands" -> LANDS
                "properties" -> PROPERTIES
                else -> GENERAL
            }
        }
    }
}

@Parcelize
data class LocalizedModel(
    val ar: String,
    val en: String
) : Parcelable {
    val localized: String
        get() = if (Helper.language == AppLanguage.English) en else ar
}

@Parcelize
data class CategoryType(
    val _id: String,
    val title: LocalizedModel
) : Parcelable

@Parcelize
data class Subcategory(
    val _id: String,
    val title: LocalizedModel
) : Parcelable