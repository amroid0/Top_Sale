package com.aelzohry.topsaleqatar.ui.ad_details.relatedAds

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.aelzohry.topsaleqatar.R
import com.aelzohry.topsaleqatar.databinding.ActivityRelatedAdsBinding
import com.aelzohry.topsaleqatar.model.Ad
import com.aelzohry.topsaleqatar.repository.Repository
import com.aelzohry.topsaleqatar.repository.remote.RemoteRepository
import com.aelzohry.topsaleqatar.ui.ads.AdsAdapter
import com.aelzohry.topsaleqatar.utils.CustomFrame
import com.aelzohry.topsaleqatar.utils.base.BaseActivity
import com.aelzohry.topsaleqatar.utils.base.BaseAdapter
import kotlin.math.roundToInt

class RelatedAdsActivity : BaseActivity<ActivityRelatedAdsBinding, RelatedAdsViewModel>() {

    private var repository: Repository = RemoteRepository()
    private lateinit var adId: String

    override fun getLayoutID(): Int = R.layout.activity_related_ads

    override fun getViewModel(): RelatedAdsViewModel =
        ViewModelProvider(this)[RelatedAdsViewModel::class.java]

    override fun setupUI() {
        adId = intent.getStringExtra(AD_ID) ?: ""
        initToolbar(getString(R.string.related_ads))

        loadRelatedAds()


    }

    private fun loadRelatedAds() {
        repository.getRelatedAds(adId) {
            vm.frameState.set(CustomFrame.FrameState.LAYOUT)
            it?.data?.let { response ->
                if (response.ads.isNotEmpty()) {
                    vm.resAds.postValue(response.ads)

                }
            }
        }
    }

    override fun onClickedListener() {


    }

    override fun observerLiveData() {

        var itemWidth = 0
        screenSizeInDp.apply {
            // screen width in dp
            val scale = resources.displayMetrics.density
            val dpAsPixels = (16.0f * scale + 0.5f).toInt()
            itemWidth = (x - dpAsPixels) / 3
        }

        vm.resAds.observe(this, Observer {
            val adapter = AdsAdapter(itemWidth,{}, vm)
            adapter.addAds(it)
            binding.rv.adapter = adapter
        })

    }

    companion object {

        const val AD_ID = "ad_id"
        fun newInstance(context: Context, adId: String) {
            context.startActivity(
                Intent(context, RelatedAdsActivity::class.java)
                    .putExtra(AD_ID, adId)
            )
        }
    }


    val Activity.displayMetrics: DisplayMetrics
        get() {
            // display metrics is a structure describing general information
            // about a display, such as its size, density, and font scaling
            val displayMetrics = DisplayMetrics()

            if (Build.VERSION.SDK_INT >= 30){
                display?.apply { getRealMetrics(displayMetrics)
                }
            }else{
                // getMetrics() method was deprecated in api level 30
                windowManager.defaultDisplay.getMetrics(displayMetrics)
            }

            return displayMetrics
        }

    val Activity.screenSizeInDp: Point
        get() {
            val point = Point()
            displayMetrics.apply {
                // screen width in dp
                point.x = (widthPixels / density).roundToInt()

                // screen height in dp
                point.y = (heightPixels / density).roundToInt()
            }

            return point
        }
}