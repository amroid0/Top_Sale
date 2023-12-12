package com.aelzohry.topsaleqatar.ui.ad_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aelzohry.topsaleqatar.model.Ad

/*
* created by : ahmed mustafa
* email : ahmed.mustafa15996@gmail.com
*phone : +201025601465
*/
class ViewModelFactory(private val ad: Ad?, private val adId: String) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = AdDetailsViewModel(ad = ad, adId = adId) as T
}