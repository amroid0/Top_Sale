package com.aelzohry.topsaleqatar.ui.ad_details.relatedAds

import androidx.lifecycle.MutableLiveData
import com.aelzohry.topsaleqatar.model.Ad
import com.aelzohry.topsaleqatar.utils.base.BaseViewModel

/*
* created by : ahmed mustafa
* email : ahmed.mustafa15996@gmail.com
*phone : +201025601465
*/
class RelatedAdsViewModel : BaseViewModel() {

     val resAds = MutableLiveData<List<Ad>>()
}