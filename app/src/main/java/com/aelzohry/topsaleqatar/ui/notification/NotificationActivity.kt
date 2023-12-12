package com.aelzohry.topsaleqatar.ui.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aelzohry.topsaleqatar.R
import com.aelzohry.topsaleqatar.databinding.ActivityNotificationBinding
import com.aelzohry.topsaleqatar.helper.Constants
import com.aelzohry.topsaleqatar.helper.Helper
import com.aelzohry.topsaleqatar.helper.NotificationCenter
import com.aelzohry.topsaleqatar.helper.NotificationType
import com.aelzohry.topsaleqatar.helper.showProgress
import com.aelzohry.topsaleqatar.model.Not
import com.aelzohry.topsaleqatar.repository.Repository
import com.aelzohry.topsaleqatar.repository.remote.RemoteRepository
import com.aelzohry.topsaleqatar.repository.remote.responses.NotificationsResponse
import com.aelzohry.topsaleqatar.ui.ads.AdsFragment
import com.aelzohry.topsaleqatar.ui.messages.NotWrapper
import com.aelzohry.topsaleqatar.ui.messages.adapter.NotificationsAdapter
import com.aelzohry.topsaleqatar.ui.user.UserFragment
import com.aelzohry.topsaleqatar.utils.CustomFrame
import com.aelzohry.topsaleqatar.utils.DividerItemDecorator
import com.aelzohry.topsaleqatar.utils.base.BaseActivity
import retrofit2.Call

class NotificationActivity : BaseActivity<ActivityNotificationBinding, NotificationViewModel>() {

    private lateinit var repository: Repository
    private lateinit var adapter: NotificationsAdapter

    private var isLoading = false
    private var currentPage = 1
    private var didLoadAllNotifications = false

    private var notificationsCall: Call<NotificationsResponse>? = null

    companion object {
        fun newInstance(context: Context): Intent {
            return Intent(context, NotificationActivity::class.java)
        }

    }

    override fun getLayoutID(): Int = R.layout.activity_notification

    override fun getViewModel(): NotificationViewModel = ViewModelProvider(this)[NotificationViewModel::class.java]

    override fun setupUI() {

        initToolbar(getString(R.string.notifications_tab))

        repository = RemoteRepository()

        vm.frameState.set(CustomFrame.FrameState.LAYOUT)
        binding.refreshLayout.setOnRefreshListener { refresh() }
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0)
                // check for scroll down
                {
                    val layoutManager = recyclerView.layoutManager as? LinearLayoutManager ?: return
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val pastVisibleItems = layoutManager.findFirstVisibleItemPosition()

                    if (!isLoading && !didLoadAllNotifications) {
                        if (visibleItemCount + pastVisibleItems >= totalItemCount) {
                            loadNextPage()
                        }
                    }
                }
            }
        })

        binding.recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.recyclerView.addItemDecoration(DividerItemDecorator(ContextCompat.getDrawable(this, R.drawable.divider_line)))
        adapter = NotificationsAdapter(this, arrayListOf()) {
            didTapNotWrapper(it)
        }
        binding.recyclerView.adapter = adapter

        NotificationCenter.addObserver(this, NotificationType.NewNotificationReceived, newNotificationReceiver)
        refresh()
    }

    override fun onClickedListener() {
    }

    override fun observerLiveData() {
    }

    private fun loadNotification(page: Int) {
        notificationsCall = repository.getNotifications(page) { success, message, notifications ->
            if (success) {
                notifications?.let { nots ->
                    currentPage = page
                    val wrappers = nots.map { NotWrapper(it.createdAt, it, null) }
                    adapter.wrappers.addAll(wrappers)
                    adapter.sort()
                }
                if (notifications.isNullOrEmpty()) {
                    didLoadAllNotifications = true
                }
            } else {
                Helper.showToast(this, message)
            }
            endLoading()
        }
    }

    private fun loadNextPage() {
        if (isLoading) return
        loadData(currentPage + 1)
    }

    private fun loadData(page: Int) {
        if (isLoading) return
        startLoading()
        loadNotification(page)
    }

    private fun startLoading() {
        isLoading = true
        binding.refreshLayout.isRefreshing = true
    }

    private fun endLoading() {
        isLoading = false
        binding.refreshLayout.isRefreshing = false
    }

    private fun refresh() {
        isLoading = false
        didLoadAllNotifications = false
        currentPage = 1
        adapter.wrappers.clear()
        adapter.notifyDataSetChanged()
        loadData(1)
    }

    private fun didTapNotWrapper(wrapper: NotWrapper) {
        wrapper.not?.let { not ->
            if (!not.seen) {
                not.seen = true
                adapter.notifyDataSetChanged()
            }
            didTapNot(not)
        }
    }

    private fun didTapNot(not: Not) {
        when {
            not.objectCategory != null -> {
                AdsFragment.newInstance(this, Constants.FROM_OTHERS, not.objectCategory)
            }

            not.objectAd != null -> {
                Log.e("ad_details_chat", not.objectAd.toString())
                vm.onAdClickedListener(binding.root, not.objectAd)
            }

            not.objectUser != null -> {
                UserFragment.newInstance_(this, not.objectUser, null, not.objectUser._id)
//                activity.pushFragment(fragment)
            }

            not.objectComment != null -> {
                val adId = not.objectComment.adId ?: return
                val progress = showProgress(this)
                repository.getAd(adId) {
                    progress.dismiss()
                    it?.let { ad ->
                        vm.onAdClickedListener(binding.root, ad)
//                        AdDetailsFragment.newInstance(requireContext(), ad)
//                        activity.pushFragment(fragment)
                    }
                }
            }
        }
    }

    private val newNotificationReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val newNotification = intent.getParcelableExtra<Not>("new_notification") ?: return
            val wrapper = adapter.wrappers.firstOrNull { it.not?._id == newNotification._id }
            if (wrapper == null) {
                adapter.wrappers.add(0, NotWrapper(newNotification.createdAt, newNotification, null))
                adapter.notifyDataSetChanged()
            } else {
                refresh()
            }
        }
    }

    override fun onDestroy() {
        notificationsCall?.cancel()
        NotificationCenter.removeObserver(this, newNotificationReceiver)
        super.onDestroy()
    }

}