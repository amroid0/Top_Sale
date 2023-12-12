package com.aelzohry.topsaleqatar.model

import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.aelzohry.topsaleqatar.BR
import com.aelzohry.topsaleqatar.helper.AppLanguage
import com.aelzohry.topsaleqatar.helper.Helper
import kotlinx.android.parcel.Parcelize

/*
* created by : ahmed mustafa
* email : ahmed.mustafa15996@gmail.com
*phone : +201025601465
*/

@Parcelize
class StanderModel(
    val _id: String,
    val title: String,
    var isChecked: Boolean = false
) : BaseObservable(), Parcelable {

    var is_checked: Boolean
        @Bindable get() = isChecked
        set(value) {
            isChecked = value
            notifyPropertyChanged(BR._checked)
        }

}