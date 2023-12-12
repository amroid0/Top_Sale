package com.aelzohry.topsaleqatar.ui.new_ad.adLocation

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ObservableField
import com.aelzohry.topsaleqatar.utils.base.BaseViewModel

/*
* created by : ahmed mustafa
* email : ahmed.mustafa15996@gmail.com
*phone : +201025601465
*/
class AdLocationViewModel : BaseViewModel() {

    var lat = 0.0
    var lng = 0.0
    var etAddress = ObservableField("")

    fun onSelectLocationClickedListener(v: View) {

        val activity = v.context as AppCompatActivity

        activity.setResult(
            Activity.RESULT_OK, Intent()
                .putExtra("lat", lat)
                .putExtra("lng", lng)
                .putExtra("address", etAddress.get() ?: "")
        )

        activity.finish()
    }
}