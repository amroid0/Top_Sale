package com.aelzohry.topsaleqatar.utils.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import androidx.navigation.Navigation
import com.aelzohry.topsaleqatar.R
import com.aelzohry.topsaleqatar.helper.Helper
import com.aelzohry.topsaleqatar.model.Ad
import com.aelzohry.topsaleqatar.model.AdStatus
import com.aelzohry.topsaleqatar.model.Banner
import com.aelzohry.topsaleqatar.repository.remote.RemoteRepository
import com.aelzohry.topsaleqatar.ui.ad_details.AdDetailsActivity
import com.aelzohry.topsaleqatar.ui.messages.ChatFragment
import com.aelzohry.topsaleqatar.utils.CustomFrame
import com.aelzohry.topsaleqatar.utils.SingleLiveEvent
import gun0912.tedimagepicker.util.ToastUtil
import io.branch.indexing.BranchUniversalObject
import io.branch.referral.BranchError
import io.branch.referral.util.ContentMetadata
import io.branch.referral.util.LinkProperties

open class BaseViewModel : ViewModel(), LifecycleObserver {

    private var repository = RemoteRepository()

    var showToast = SingleLiveEvent<Any>().apply { value = null }
    var snackLogin = SingleLiveEvent<Boolean>().apply { value = false }
    var isLoading = SingleLiveEvent<Boolean>().apply { value = false }
    var swipeRefresh = ObservableField(false)
    var frameState = ObservableField(CustomFrame.FrameState.PROGRESS).apply { set(CustomFrame.FrameState.PROGRESS) }
    var isLogin = Helper.isAuthenticated
    var isAdViewGrid = ObservableField(true)

    var notificationNumberRes = MutableLiveData<Int>()

    init {
        isAdViewGrid.set(true)

        Helper.getUnSeenNotificationNumber(repository, object : Helper.Listener {
            override fun passUnSeenNotificationNumber(notificationNumber: Int, messagesNumber: Int) {
                notificationNumberRes.postValue(notificationNumber)
            }
        })
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun refreshData() {
        isLogin = Helper.isAuthenticated
    }

    fun onAdClickedListener(v: View, model: Ad) {
        if (model.status == AdStatus.ACTIVE){
            AdDetailsActivity.newInstance(v.context, model, isClear = false)
        }else{
            ToastUtil.showToast(v.context.getString(R.string.ad_not_found))
        }
    }

    fun onWhatsappClick(v: View, ad: Ad) {
        val mobile = ad.user?.mobile ?: return
        val url = "https://wa.me/${mobile}"
        Helper.openUrl(url, v.context)
    }

    fun onChatClick(v: View, ad: Ad) {

        isLoading.postValue(true)
        repository.newChannel(ad._id, null) { channel, message ->
            isLoading.postValue(false)
            if (channel == null) {
                showToast.postValue(message)
                return@newChannel
            }

            ChatFragment.newInstance(v.context, channel, ad.user?.mobile, true)
        }
    }

    fun onCallClick(v: View, model: Ad) {
        val phone = model.user?.mobile ?: return
        Helper.callPhone(phone, v.context)
    }

    fun onAdBannerClickedListener(v: View, banner: Banner) {

        if (banner.ad != null) {
            onAdClickedListener(v, banner.ad)
            return
        }

        if (banner.url != null) {
            Helper.openUrl(banner.url, v.context)
            return
        }

    }

    fun onShareAdClick(v: View, model: Ad) {
        initDeepLink(v,model)
    }

    open fun onBackClickedListener(v: View) {
        Navigation.findNavController(v).popBackStack()
    }

    fun initDeepLink(view: View, ad: Ad?) {

        val lp = LinkProperties().setChannel("facebook").setFeature("sharing")

        ad?.let {
            val buo = BranchUniversalObject().setCanonicalIdentifier("content/12345").setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC).setLocalIndexMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC).setContentMetadata(ContentMetadata().addCustomMetadata("ad_id", it.id))

            var title = ""
//            var title = "تمت مشاركة هذا الإعلان '${it.title}' معك من تطبيق توب سيل"
            title = title + "\n" + it.title
            buo.setTitle(title)
            buo.setContentDescription(it.details)

            if (it.adPhoto.isNotEmpty()) {
                buo.setContentImageUrl(it.adPhoto.get(0).orgUrl)
            }


            buo.generateShortUrl(view.context, lp) { url: String, error: BranchError? ->
                if (error == null) {
                    shareLink(url, view.context)
                    Log.i("MyApp", "got my Branch link to share: $url")
                }
            }

        }


    }

    fun shareLink(url: String, mActivity: Context) {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, mActivity.getString(R.string.app_name))
            shareIntent.putExtra(Intent.EXTRA_TEXT, url)
            mActivity.startActivity(Intent.createChooser(shareIntent, "choose one"))
        } catch (e: Exception) {
            //e.toString();
        }
    }

}