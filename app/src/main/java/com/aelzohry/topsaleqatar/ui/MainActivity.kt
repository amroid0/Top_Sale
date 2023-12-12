package com.aelzohry.topsaleqatar.ui

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.aelzohry.topsaleqatar.R
import com.aelzohry.topsaleqatar.databinding.ActivityMainBinding
import com.aelzohry.topsaleqatar.helper.Helper
import com.aelzohry.topsaleqatar.helper.SocketHelper
import com.aelzohry.topsaleqatar.repository.remote.RemoteRepository
import com.aelzohry.topsaleqatar.ui.ad_details.AdDetailsActivity
import com.aelzohry.topsaleqatar.ui.messages.MessagesFragment
import com.aelzohry.topsaleqatar.ui.new_ad.NewAdFragment
import com.aelzohry.topsaleqatar.ui.user.UserFragment
import com.aelzohry.topsaleqatar.utils.GPSTracker
import com.aelzohry.topsaleqatar.utils.base.BaseActivity
import com.aelzohry.topsaleqatar.utils.base.BaseViewModel
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.onesignal.OSSubscriptionObserver
import com.onesignal.OSSubscriptionStateChanges
import com.onesignal.OneSignal


class MainActivity : BaseActivity<ActivityMainBinding, BaseViewModel>(), OSSubscriptionObserver,
    BottomNavigationView.OnNavigationItemSelectedListener, MessagesFragment.Listener {

    private lateinit var currentLocation: GPSTracker
    private lateinit var navController: NavController

    override fun getLayoutID(): Int = R.layout.activity_main

    override fun getViewModel(): BaseViewModel = ViewModelProvider(this)[BaseViewModel::class.java]

    override fun setupUI() {
        setOneSignalListener()
        setupShareLinks()
        setupNavController()
        //if the user is not Authenticated the app crash so i handle it with return void for now
//        if (!Helper.isAuthenticated) {
//            return
//        }

        if (Helper.isAuthenticated)
            SocketHelper.connect()

        OneSignal.addSubscriptionObserver(this)


        OneSignal.getDeviceState()?.userId?.let {
            Helper.setPushUserId(it)
        }



    }

    fun showOrHideBadge(notificationNumber: Int,messagesNumber: Int) {
        if (messagesNumber == 0) {
            removeBadge(binding.bottomNavigation, R.id.chatFragment)
        } else {
            showBadge(this, binding.bottomNavigation, R.id.chatFragment, messagesNumber.toString())
        }

    }

    fun showBadge(
        context: Context?,
        bottomNavigationView: BottomNavigationView,
        @IdRes itemId: Int,
        value: String?
    ) {
        removeBadge(bottomNavigationView, itemId)
        val itemView: BottomNavigationItemView = bottomNavigationView.findViewById(itemId)
        val badge: View = LayoutInflater.from(context)
            .inflate(
                R.layout.layout_notification_badge,
                bottomNavigationView,
                false
            )
        val text: TextView = badge.findViewById(R.id.badge_text_view)
        text.text = value
        itemView.addView(badge)
    }

    fun removeBadge(bottomNavigationView: BottomNavigationView, @IdRes itemId: Int) {
        val itemView: BottomNavigationItemView = bottomNavigationView.findViewById(itemId)
        if (itemView.childCount == 3) {
            itemView.removeViewAt(2)
        }
    }

//    private fun requestCurrentLocation() {
//        currentLocation = GPSTracker(this){}
//        lifecycle.addObserver(currentLocation)
//    }

    private fun setupShareLinks() {

        val data = intent.data?.path?.split("/")
//        vm.showToast.postValue("data_size"+data?.toString())
        val id = data?.last()
        if (id != null) {
            val size = data?.size ?: 0
            if (size == 0 || id?.isEmpty() == true)
                return

            when (data?.get(size.minus(2))) {
                "ads" -> AdDetailsActivity.newInstance(
                    this, null,
                    cameFromChat = false,
                    isClear = false,
                    adId = id
                )//vm.showToast.postValue("ad_id : $id")
                "users" -> {
                    UserFragment.newInstance_(
                        this,
                        null,
                        null,
                        id
                    )

                    //vm.showToast.postValue("users : $id")
                }
            }
        }

    }

    private fun setupNavController() {

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        navController = Navigation.findNavController(this, R.id.navFragment)

        binding.bottomNavigation.setupWithNavController(navController)
        binding.bottomNavigation.setOnNavigationItemSelectedListener(this)
        NavigationUI.setupWithNavController(binding.toolbar, navController)
        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    override fun onClickedListener() {
        binding.fabAdd.setOnClickListener { onAddAdButtonClickedListener(this) }
    }

    fun onAddAdButtonClickedListener(context: Context) {
        if (vm.isLogin) {
            context.startActivity(NewAdFragment.newAd(context))
        } else vm.snackLogin.postValue(true)

    }

    override fun observerLiveData() {
//        vm.unSeenNotificationNumber.observe(this, {
//            showOrHideBadge(number = it)
//        })


        Helper.getUnSeenNotificationNumber(RemoteRepository(), object : Helper.Listener {
            override fun passUnSeenNotificationNumber(notificationNumber: Int,messagesNumber: Int) {
                showOrHideBadge(notificationNumber,messagesNumber)
            }
        })
    }


    fun setOneSignalListener() {
        OneSignal.setNotificationWillShowInForegroundHandler {
            Log.e("test_notification", "new notification")
            loadNumberOfUnSeenNotification()
        }
    }

    fun loadNumberOfUnSeenNotification() {
        val repository = RemoteRepository()
        Helper.getUnSeenNotificationNumber(repository, object : Helper.Listener {
            override fun passUnSeenNotificationNumber(notificationNumber: Int,messagesNumber: Int) {
                showOrHideBadge(notificationNumber,messagesNumber)
            }
        })
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        loadNumberOfUnSeenNotification()
        when (item.itemId) {
            R.id.homeFragment, R.id.profileFragment,R.id.categoriesFragment -> {
                navController.navigate(item.itemId)
                return true
            }


//            R.id.addAdFragment -> {
//                if (vm.isLogin)
//                   startActivity( NewAdFragment.newAd(this))
//                else {
//                    vm.snackLogin.postValue(true)
//                }
//                return false
//            }
            R.id.chatFragment, R.id.myAdsFragment -> {
                return if (vm.isLogin) {
                    navController.navigate(item.itemId)
                    true
                } else {
                    vm.snackLogin.postValue(true)
                    false
                }
            }
        }
        return false
    }

//    fun setOneSignalListener(){
//        OneSignal.setNotificationWillShowInForegroundHandler {
//            vm.loadNumberOfUnSeenNotification()
//        }
//    }

    override fun onBadgeChange(number: Int) {
        showOrHideBadge(number,0)
    }

    override fun onResume() {
        super.onResume()
        loadNumberOfUnSeenNotification()
    }

    override fun onOSSubscriptionChanged(stateChanges: OSSubscriptionStateChanges) {
        if (!stateChanges.from.isSubscribed &&
            stateChanges.to.isSubscribed
        ) {
            // get player ID
            val userId = stateChanges.to.userId
            Helper.setPushUserId(userId)
            Log.i("MainActivity", "OneSignal UserId: $userId")
        }
        Log.i("MainActivity", "onOSPermissionChanged: $stateChanges")
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        currentLocation.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


}
