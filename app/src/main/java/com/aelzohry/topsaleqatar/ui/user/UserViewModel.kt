package com.aelzohry.topsaleqatar.ui.user

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.aelzohry.topsaleqatar.R
import com.aelzohry.topsaleqatar.model.Ad
import com.aelzohry.topsaleqatar.model.User
import com.aelzohry.topsaleqatar.repository.Repository
import com.aelzohry.topsaleqatar.repository.remote.AdsDataSource
import com.aelzohry.topsaleqatar.repository.remote.RemoteRepository
import com.aelzohry.topsaleqatar.ui.MainActivity
import com.aelzohry.topsaleqatar.ui.messages.ChatFragment
import com.aelzohry.topsaleqatar.ui.more.followers.FollowersFragment
import com.aelzohry.topsaleqatar.ui.more.followings.FollowingsFragment
import com.aelzohry.topsaleqatar.utils.CustomFrame
import com.aelzohry.topsaleqatar.utils.base.BaseViewModel
import com.aelzohry.topsaleqatar.utils.base.BetterActivityResult

/*
* created by : ahmed mustafa
* email : ahmed.mustafa15996@gmail.com
*phone : +201025601465
*/
class UserViewModel(user: User?, val userId: String?) : BaseViewModel() {

    private var repository: Repository = RemoteRepository()

    var followState = ObservableField(false)
    var followingCount = ObservableField("")
    var followersCount = ObservableField("")
    var adsCount = ObservableField("")
    var adsRes: LiveData<PagedList<Ad>>
    var userRes = MutableLiveData<User>()

    private val dataSourceFactory: AdsDataSource.DataSourceFactory

    init {

        user?.let {
            userRes.postValue(it)
        }
        val prams = HashMap<String, Any>()
        prams["user"] = userId ?: ""


        dataSourceFactory = AdsDataSource.DataSourceFactory(prams, AdsDataSource.USER_ADS)
        val pageSize = 10
        adsRes = LivePagedListBuilder(
            dataSourceFactory,
            PagedList.Config.Builder()
                .setPageSize(pageSize)
                .setInitialLoadSizeHint(pageSize * 2)
                .setEnablePlaceholders(false)
                .build()
        ).build()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        userId ?: return
        repository.getUser(userId) {
            it?.let {
                userRes.postValue(it)
            }
        }
    }


    fun onBlocUserClickedListener(v: View) {
        if (!isLogin) {
            snackLogin.postValue(true)
            return
        }

        isLoading.postValue(true)
        repository.blockUser(userId ?: "") { success, message ->
            showToast.postValue(message)
            isLoading.postValue(false)
            if (success) {
                v.context.startActivity(
                    Intent(v.context, MainActivity::class.java)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            }
        }
    }

    fun onBlocUserClicked(ctx: Context) {
        if (!isLogin) {
            snackLogin.postValue(true)
            return
        }

        isLoading.postValue(true)
        repository.blockUser(userId ?: "") { success, message ->
            showToast.postValue(message)
            isLoading.postValue(false)
            if (success) {
                ctx.startActivity(
                    Intent(ctx, MainActivity::class.java)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            }
        }
    }

    fun onFollowClickedListener(activity: Activity) {
        if (!isLogin) {
            snackLogin.postValue(true)
            return
        }

        isLoading.postValue(true)

        if (followState.get() == true) {
            repository.unFollowUser(userId ?: "") { success, message ->
                if (success) {
                    followState.set(false)
                    activity.setResult(RESULT_OK)
                    onCreate()
                }
                showToast.postValue(message)
                isLoading.postValue(false)
            }
        } else {
            repository.followUser(userId ?: "") { success, message ->
                if (success) {
                    followState.set(true)
                    activity.setResult(RESULT_OK)
                    onCreate()
                }
                showToast.postValue(message)
                isLoading.postValue(false)
            }
        }
    }

    fun onFollowerClickedListener(
        v: View, activityLauncher: BetterActivityResult<Intent, ActivityResult>
    ) {
        val intent: Intent = FollowersFragment.newInstance(v.context, userId)
        activityLauncher.launch(intent) { result ->
            if (result.resultCode == RESULT_OK) {
                onCreate()

            }
        }
    }

    fun onFollowingClickedListener(
        v: View,
        activityLauncher: BetterActivityResult<Intent, ActivityResult>
    ) {
        val intent: Intent = FollowingsFragment.newInstance(v.context, userId)
        activityLauncher.launch(intent) { result ->
            if (result.resultCode == RESULT_OK) {
                onCreate()
            }
        }

    }

    fun onShareBtnClickedListener(v: View) {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        val shareBody =
            "https://topsale.qa/users/$userId"
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
        v.context.startActivity(
            Intent.createChooser(
                sharingIntent,
                v.context.getString(R.string.share_profile)
            )
        )
    }


    fun onChatClickedListener(context: Context, mobile: String) {

        isLoading.postValue(true)
        repository.newChannel(null, userId) { channel, message ->
            isLoading.postValue(false)
            if (channel == null) {
                showToast.postValue(message)
                return@newChannel
            }

            ChatFragment.newInstance(context, channel, mobile, false)
        }
    }

    fun getLayoutState(): LiveData<CustomFrame.FrameState> =
        Transformations.switchMap<AdsDataSource, CustomFrame.FrameState>(
            dataSourceFactory.dataSourceLiveData,
            AdsDataSource::layoutState
        )

    fun getFooterState(): LiveData<Boolean> =
        Transformations.switchMap<AdsDataSource, Boolean>(
            dataSourceFactory.dataSourceLiveData,
            AdsDataSource::footerState
        )
}