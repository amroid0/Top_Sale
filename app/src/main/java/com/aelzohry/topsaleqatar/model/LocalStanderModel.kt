package com.aelzohry.topsaleqatar.model

import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.aelzohry.topsaleqatar.BR
import com.aelzohry.topsaleqatar.helper.AppLanguage
import com.aelzohry.topsaleqatar.helper.Helper
import kotlinx.android.parcel.Parcelize


@Parcelize
data class LocalStanderModel(
    val _id: String,
    val title: LocalizedModel,
    var isChecked: Boolean = false
) : BaseObservable(), Parcelable {

    var is_checked: Boolean
        @Bindable get() = isChecked
        set(value) {
            isChecked = value
            notifyPropertyChanged(BR._checked)
        }

    @Parcelize
    data class LocalizedModel(
        val ar: String?,
        val en: String?
    ) : Parcelable {
        val localized: String
            get() = if (Helper.language == AppLanguage.English) {
                en ?: ""
            } else {
                ar ?: ""
            }
    }
}