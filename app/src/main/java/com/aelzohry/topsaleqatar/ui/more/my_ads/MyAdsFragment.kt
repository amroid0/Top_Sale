package com.aelzohry.topsaleqatar.ui.more.my_ads

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.PopupMenu
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.aelzohry.topsaleqatar.R
import com.aelzohry.topsaleqatar.databinding.FragmentMyAdsBinding
import com.aelzohry.topsaleqatar.databinding.ViewHolderMyAdBinding
import com.aelzohry.topsaleqatar.helper.Helper
import com.aelzohry.topsaleqatar.helper.showProgress
import com.aelzohry.topsaleqatar.model.Ad
import com.aelzohry.topsaleqatar.repository.remote.RemoteRepository
import com.aelzohry.topsaleqatar.ui.messages.MessagesFragment
import com.aelzohry.topsaleqatar.ui.new_ad.NewAdFragment
import com.aelzohry.topsaleqatar.ui.notification.NotificationActivity
import com.aelzohry.topsaleqatar.utils.base.BaseFragment
import com.aelzohry.topsaleqatar.utils.base.BasePagedAdapter
import com.aelzohry.topsaleqatar.utils.extenions.setVisible
import com.onesignal.OneSignal

class MyAdsFragment : BaseFragment<FragmentMyAdsBinding, MyAdsViewModel>() {

    private lateinit var mListener: MessagesFragment.Listener

    private lateinit var adapter: BasePagedAdapter<Ad, ViewHolderMyAdBinding>
    override fun getLayoutID(): Int = R.layout.fragment_my_ads

    override fun getViewModel(): MyAdsViewModel = ViewModelProvider(this)[MyAdsViewModel::class.java]

    override fun setupUI() {
        setOneSignalListener()
//        initToolbar(getString(R.string.my_ads))
        binding.toolbarTitle.text = getString(R.string.my_ads)
        adapter = BasePagedAdapter(R.layout.view_holder_my_ad, vm, { oldItem, newItem -> oldItem == newItem }, {
                oldItem, newItem -> oldItem._id == newItem._id }) {
                bind, model, position, adapter ->
            bind.btnEdit.setOnClickListener {
                onEditAdClick(model)
            }

            bind.btnOptions.setOnClickListener {

                val popupMenu = PopupMenu(requireContext(), it)
                val menu = popupMenu.menu
                popupMenu.menuInflater.inflate(R.menu.my_ads_option_menu, menu)
                menu.findItem(R.id.btn_active_ad).title = getString(if (model.isActive == true) R.string.deactivate_ad else R.string.activate_ad)
                menu.findItem(R.id.btn_active_ad).setVisible(false)
                model.canRepublish?.let { it1 -> menu.findItem(R.id.btn_republish).setVisible(it1) }
                popupMenu.setOnMenuItemClickListener {

                    when (it.itemId) {
                        R.id.btn_delete_ad -> {
                            deleteAd(model)
                        }

                        R.id.btn_active_ad -> {
                            deActive(model) {
                                model.isActive = if (model.isActive == true) false else true
                            }
                        }

                        R.id.btn_republish -> {
                            if (model.canRepublish == true) rePublish(model)
                        }
                    }
                    return@setOnMenuItemClickListener true
                }

                popupMenu.show()

//                val d = Dialog(requireContext())
//                setupDialog(
//                    arrayListOf(
//                        getString(R.string.delete_ad),
//                        getString(if (model.isActive == true) R.string.deactivate_ad else R.string.activate_ad)
//                    ), getString(R.string.select)
//                ) { i ->
//
//                    when (i) {
//                        0 -> deleteAd(model)
//                        1 -> deActive(model) {
//                            model.isActive = if (model.isActive == true) false else true
//                        }
//                    }
//                }
            }
        }
        binding.swipeRefreshLayout.setOnChildScrollUpCallback(object : SwipeRefreshLayout.OnChildScrollUpCallback {
            override fun canChildScrollUp(parent: SwipeRefreshLayout, child: View?): Boolean {
                if (binding.recyclerView != null) {
                    return binding.recyclerView.canScrollVertically(-1)
                }
                return false
            }
        })

        binding.recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
        binding.recyclerView.adapter = adapter


    }

    fun onEditAdClick(model: Ad) {
        val intent: Intent = NewAdFragment.editAd(requireContext(), model)
        activityLauncher.launch(intent) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                vm.reloadMyAds()
            }
        }
    }

    override fun onClickedListener() {

        binding.ibNotification.setOnClickListener {
            startActivity(NotificationActivity.newInstance(requireContext()))
        }

        binding.ibBack.setOnClickListener {
            Navigation.findNavController(it).popBackStack()
        }

        binding.editProfile.setOnClickListener {
            vm.onEditProfileClickedListener(it)
        }

        binding.llFollowers.setOnClickListener {
            vm.onFollowerClickedListener(it, activityLauncher)
        }
        binding.llFollowings.setOnClickListener {
            vm.onFollowingClickedListener(it, activityLauncher)
        }

        binding.llShare.setOnClickListener {
            vm.initDeepLink(requireActivity(), true)
        }
    }

    override fun observerLiveData() {

        vm.getFooterState().observe(this) {
            adapter.setState(it)
        }

        vm.getLayoutState().observe(this) {
            vm.frameState.set(it)
        }

        vm.adsRes.observe(this) {
            adapter.submitList(it)
            binding.swipeRefreshLayout.isRefreshing =false
//            vm.swipeRefresh.set(false)
        }

        vm.notificationNumberRes.observe(this) { number ->
            showOrHideBadge(number)
        }
    }

    private fun deleteAd(ad: Ad) {
        val context = this.context ?: return

        val dialog = AlertDialog.Builder(context).setTitle(getString(R.string.confirmation)).setMessage(getString(R.string.delete_ad_message)).setPositiveButton(getString(R.string.delete_ad)) { dialog, _ ->
                val progress = showProgress(context)
                vm.onRemoveClickedListener(ad) {
                    progress.dismiss()
                }

            }.setNegativeButton(getString(R.string.back)) { _, _ -> }.create()

        dialog.show()
    }

    fun deActive(ad: Ad, result: () -> Unit) {
        val context = this.context ?: return
        val progress = showProgress(context)
        vm.onDeActiveClickedListener(ad) {
            progress.dismiss()
            result()
        }
    }

    fun rePublish(ad: Ad) {
        val context = this.context ?: return
        val progress = showProgress(context)
        vm.onRePublishClickedListener(ad) {
            progress.dismiss()
            vm.reloadMyAds()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            mListener = context as MessagesFragment.Listener
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + "must implement the SendMessageListener interface")
        }
    }

    fun setOneSignalListener() {
        OneSignal.setNotificationWillShowInForegroundHandler {
            loadNumberOfUnSeenNotification()
        }
    }

    fun loadNumberOfUnSeenNotification() {
        val repository = RemoteRepository()
        Helper.getUnSeenNotificationNumber(repository, object : Helper.Listener {
            override fun passUnSeenNotificationNumber(notificationNumber: Int, messagesNumber: Int) {
                showOrHideBadge(notificationNumber)
                if (::mListener.isInitialized) mListener.onBadgeChange(notificationNumber)
            }
        })
    }

    private fun showOrHideBadge(number: Int) {
        if (::mListener.isInitialized) mListener.onBadgeChange(number)
        binding.tvNotificationBadge.text = number.toString()
        binding.tvNotificationBadge.setVisible(number != 0)
    }
}
