package com.aelzohry.topsaleqatar.ui.home

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import com.aelzohry.topsaleqatar.helper.Constants.FROM_OTHERS
import com.aelzohry.topsaleqatar.helper.Helper
import com.aelzohry.topsaleqatar.helper.hideKeyboard
import com.aelzohry.topsaleqatar.model.Ad
import com.aelzohry.topsaleqatar.model.Banner
import com.aelzohry.topsaleqatar.model.TopBanner
import com.aelzohry.topsaleqatar.model.TopBannerType
import com.aelzohry.topsaleqatar.repository.Repository
import com.aelzohry.topsaleqatar.repository.remote.RemoteRepository
import com.aelzohry.topsaleqatar.repository.remote.responses.BannerSliderResponse
import com.aelzohry.topsaleqatar.repository.remote.responses.BannersResponse
import com.aelzohry.topsaleqatar.repository.remote.responses.HomeResponse
import com.aelzohry.topsaleqatar.ui.ads.AdsAdapter
import com.aelzohry.topsaleqatar.ui.ads.AdsFragment
import com.aelzohry.topsaleqatar.ui.categories.CategoriesActivity
import com.aelzohry.topsaleqatar.ui.new_ad.NewAdFragment
import com.aelzohry.topsaleqatar.ui.search.SearchFragment
import com.aelzohry.topsaleqatar.utils.CustomFrame
import com.aelzohry.topsaleqatar.utils.base.BaseViewModel
import retrofit2.Call

/*
* created by : ahmed mustafa
* email : ahmed.mustafa15996@gmail.com
*phone : +201025601465
*/
class HomeViewModel : BaseViewModel() {

    private var repository: Repository = RemoteRepository()
    private var homeCall: Call<HomeResponse>? = null
    private var bannersCall: Call<BannersResponse>? = null
    private var bannersSliderCall: Call<BannerSliderResponse>? = null
    lateinit var adsAdapter: AdsAdapter


    var adsRes = MutableLiveData<ArrayList<Ad>>()
    var adsBannerRes = MutableLiveData<ArrayList<Banner>>()
    var bannerSliderRes = MutableLiveData<ArrayList<TopBanner>>()
    var topBannersRes = MutableLiveData<ArrayList<TopBanner>>()

    var etSearch = ObservableField("")

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun loadData() {
        loadData(false)
        loadCategory()
    }

    private fun loadData(isSwipe: Boolean) {

        if (isSwipe) swipeRefresh.set(true)
        frameState.set(CustomFrame.FrameState.PROGRESS)
        homeCall = repository.getHome {
            frameState.set(CustomFrame.FrameState.LAYOUT)
            it?.data?.let { data ->
                topBannersRes.postValue(data.banners)
                Log.e("test_size", data.ads.size.toString())
                val list = ArrayList(data.ads.subList(0, data.ads.size - 1))
                adsRes.postValue(list)
            }
            if (isSwipe) swipeRefresh.set(false)
        }

        bannersCall = repository.getBanners {
            it?.let { banners ->
                adsBannerRes.postValue(banners)
            }
        }

        bannersSliderCall = repository.getTopSliders {
            it?.let { banners ->
                banners.data?.let {
                    Log.e("test_slider",it.banners.size.toString())
                    bannerSliderRes.postValue(it.banners)
                }
            }
        }

        Helper.getUnSeenNotificationNumber(repository, object : Helper.Listener {
            override fun passUnSeenNotificationNumber(notificationNumber: Int, messagesNumber: Int) {
                notificationNumberRes.postValue(notificationNumber)
            }
        })


    }

    private fun loadCategory() {
        repository.getCategories {
            it?.data?.let { categories ->
                Helper.setCategoryList(categories)
            }
        }
    }

    fun onRefreshListener() {
        adsAdapter.clear()
        loadData(true)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun disConnect() {
        homeCall?.cancel()
        bannersCall?.cancel()
    }

    fun onAddAdButtonClickedListener(context: Context) {
        if (isLogin) {
            context.startActivity(NewAdFragment.newAd(context))
        } else snackLogin.postValue(true)

    }

    fun onCatsButtonsClickedListener(v: View) {
        v.context.startActivity(Intent(v.context, CategoriesActivity::class.java))
//        Navigation.findNavController(v).navigate(R.id.catsFragment)
    }

    fun onSearchClickedListener(v: View) {

        val txt = etSearch.get() ?: ""
        if (txt.isEmpty()) return

        etSearch.set("")
        v.hideKeyboard()
        SearchFragment.newInstance(v.context, txt)

    }

    fun onBannerClickedListener(v: View, model: TopBanner) {

        when (model.type) {
            TopBannerType.CATEGORY -> {
                model.category?.let {
                    AdsFragment.newInstance(v.context, FROM_OTHERS, it)
                }
            }

            TopBannerType.URL -> {
                model.url?.let {
                    Helper.openUrl(it, v.context)
                }
            }

            TopBannerType.AD -> {
                model.ad?.let {
                    onAdClickedListener(v, it)
                }
            }
        }
    }
}