package com.aelzohry.topsaleqatar.ui.more.my_ads

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.databinding.ObservableField
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.aelzohry.topsaleqatar.R
import com.aelzohry.topsaleqatar.helper.Helper
import com.aelzohry.topsaleqatar.model.Ad
import com.aelzohry.topsaleqatar.model.User
import com.aelzohry.topsaleqatar.repository.remote.AdsDataSource
import com.aelzohry.topsaleqatar.repository.remote.RemoteRepository
import com.aelzohry.topsaleqatar.ui.more.followers.FollowersFragment
import com.aelzohry.topsaleqatar.ui.more.followings.FollowingsFragment
import com.aelzohry.topsaleqatar.ui.more.profile.ProfileFragment
import com.aelzohry.topsaleqatar.utils.CustomFrame
import com.aelzohry.topsaleqatar.utils.base.BaseViewModel
import com.aelzohry.topsaleqatar.utils.base.BetterActivityResult
import io.branch.indexing.BranchUniversalObject
import io.branch.referral.BranchError
import io.branch.referral.util.ContentMetadata
import io.branch.referral.util.LinkProperties

/*
* created by : ahmed mustafa
* email : ahmed.mustafa15996@gmail.com
*phone : +201025601465
*/
class MyAdsViewModel : BaseViewModel() {
    private var user: User? =null
    private val dataSourceFactory: AdsDataSource.DataSourceFactory
    private var repository = RemoteRepository()
    lateinit var adsRes: LiveData<PagedList<Ad>>

    var followingCount = ObservableField("")
    var followersCount = ObservableField("")
    var name = ObservableField("")
    var phone = ObservableField("")
    var email = ObservableField("")
    var bio = ObservableField("")
    var website = ObservableField("")
    var adsCount = ObservableField("")

    init {

        repository = RemoteRepository()
        val prams = HashMap<String, Any>()
        dataSourceFactory = AdsDataSource.DataSourceFactory(prams, AdsDataSource.MY_ADS)
        val pageSize = 10
        adsRes = LivePagedListBuilder(dataSourceFactory, PagedList.Config.Builder().setPageSize(pageSize).setInitialLoadSizeHint(pageSize * 2).setEnablePlaceholders(false).build()).build()
    }

    fun reloadMyAds() {
        dataSourceFactory.changePrams(HashMap(), AdsDataSource.MY_ADS)
    }

    fun onRefreshListener() {
        reloadMyAds()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun loadData() {
        repository.getProfile { user, message ->
            this.user= user
            user?.let {
                followersCount.set(it.stats?.followersCount.toString())
                followingCount.set(it.stats?.followingsCount.toString())
                name.set(it._name)
                phone.set(it.mobile)
                email.set(it.email)
                bio.set(it.bio)
                adsCount.set(it.stats?.adsCount.toString())
//                website.set(it.we)

            }
        }

        Helper.getUnSeenNotificationNumber(repository, object : Helper.Listener {
            override fun passUnSeenNotificationNumber(notificationNumber: Int, messagesNumber: Int) {
                notificationNumberRes.postValue(notificationNumber)
            }
        })
    }

    fun onRemoveClickedListener(ad: Ad, result: () -> Unit) {
        repository.deleteAd(ad._id) { success, message ->
            showToast.postValue(message)
            result()
            if (success) {
                dataSourceFactory.changePrams(HashMap())
            }
        }
    }

    fun onDeActiveClickedListener(ad: Ad, result: () -> Unit) {

        if (ad.isActive == true) {
            repository.deactivateAd(ad._id) { success, message, updatedAd ->
                showToast.postValue(message)
                result()
            }
        } else {
            repository.activateAd(ad._id) { success, message, updatedAd ->
                showToast.postValue(message)
                result()
            }
        }
    }

    fun onRePublishClickedListener(ad: Ad, result: () -> Unit) {
        repository.republishAd(ad._id) { success, message, updatedAd ->
            showToast.postValue(message)
            result()
        }
    }

    fun onEditProfileClickedListener(v: View) {
        ProfileFragment.newInstance(v.context)
    }

    fun onFollowerClickedListener(v: View, activityLauncher: BetterActivityResult<Intent, ActivityResult>) {
        val intent: Intent = FollowersFragment.newInstance(v.context, Helper.userId)
        activityLauncher.launch(intent) { result ->
            if (result.resultCode == RESULT_OK) {
                loadData()
            }
        }
    }

    fun onFollowingClickedListener(v: View, activityLauncher: BetterActivityResult<Intent, ActivityResult>) {
        val intent: Intent = FollowingsFragment.newInstance(v.context, Helper.userId)
        activityLauncher.launch(intent) { result ->
            if (result.resultCode == RESULT_OK) {
                loadData()
            }
        }

    }

    fun initDeepLink(mActivity: Activity, withShare: Boolean) {
        if (user == null) return

        val lp = LinkProperties().setChannel("facebook").setFeature("sharing")

        user?.let {
            val buo = BranchUniversalObject().setCanonicalIdentifier("content/12345")
                .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
                .setLocalIndexMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
                .setContentMetadata(ContentMetadata().addCustomMetadata("user_id", it._id))

            var title = ""
            title = title + "\n" + it._name
            buo.setTitle(title)
            buo.setContentDescription(it.bio)

            it.profilePhoto?.let {
                buo.setContentImageUrl(it)
            }


            buo.generateShortUrl(mActivity, lp) { url: String, error: BranchError? ->
                if (error == null) {
                    if (withShare) {
                        shareLink(url, mActivity)
                    }
                    Log.i("MyApp", "got my Branch link to share: $url")
                }
            }

        }


    }

    fun shareLink(url: String, mActivity: Activity) {
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

    fun getLayoutState(): LiveData<CustomFrame.FrameState> = Transformations.switchMap(dataSourceFactory.dataSourceLiveData, AdsDataSource::layoutState)

    fun getFooterState(): LiveData<Boolean> = Transformations.switchMap(dataSourceFactory.dataSourceLiveData, AdsDataSource::footerState)
}