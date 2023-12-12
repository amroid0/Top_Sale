package com.aelzohry.topsaleqatar.ui.user

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.os.Build
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import androidx.recyclerview.widget.GridLayoutManager
import com.aelzohry.topsaleqatar.R
import com.aelzohry.topsaleqatar.databinding.FragmentUserBinding
import com.aelzohry.topsaleqatar.helper.Helper
import com.aelzohry.topsaleqatar.model.Ad
import com.aelzohry.topsaleqatar.model.AdBanner
import com.aelzohry.topsaleqatar.model.User
import com.aelzohry.topsaleqatar.ui.adapters.AdsAdapter
import com.aelzohry.topsaleqatar.utils.Utils
import com.aelzohry.topsaleqatar.utils.base.BaseActivity
import com.aelzohry.topsaleqatar.utils.extenions.showToast
import com.ligl.android.widget.iosdialog.IOSSheetDialog
import com.ligl.android.widget.iosdialog.IOSSheetDialog.SheetItem
import com.squareup.picasso.Picasso
import kotlin.math.roundToInt

class UserFragment : BaseActivity<FragmentUserBinding, UserViewModel>() {

    companion object {
        private const val ARG_USER = "USER"
        private const val ARG_USER_ID = "USER_ID"
        private const val ARG_AD = "AD"

        fun newInstance(context: Context, user: User?, ad: Ad?, userId: String?): Intent {
            return Intent(context, UserFragment::class.java).putExtra(ARG_USER, user).putExtra(ARG_AD, ad).putExtra(ARG_USER_ID, userId)
        }

        fun newInstance_(context: Context, user: User?, ad: Ad?, userId: String?) = context.startActivity(Intent(context, UserFragment::class.java).putExtra(ARG_USER, user).putExtra(ARG_AD, ad).putExtra(ARG_USER_ID, userId))
    }

    private lateinit var user: User
    private lateinit var adapter: AdsAdapter

    override fun getLayoutID(): Int = R.layout.fragment_user

    override fun getViewModel(): UserViewModel = ViewModelProvider(this, ViewModelFactory(
        intent?.getParcelableExtra(ARG_USER),
        intent?.getStringExtra(ARG_USER_ID),
    ))[UserViewModel::class.java]

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.user_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.getItemId()
        return when (id) {
            R.id.block -> {
                vm.onBlocUserClicked(this)
                true
            }

            R.id.report -> {
                showToast(getString(R.string.action_successful))
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun setupUI() {

//        var itemWidth = 0
//        screenSizeInDp.apply {
//            // screen width in dp
//            val scale = resources.displayMetrics.density
//            val dpAsPixels = (16.0f * scale + 0.5f).toInt()
//            itemWidth = (x - dpAsPixels) / 3
//        }

        var itemWidth = 0
        screenSizeInDp.apply {
            // screen width in dp
            val itemWidthInDp = (x - 16) / 3
            itemWidth = Utils.pxFromDp(applicationContext, itemWidthInDp.toFloat()).toInt()
        }

        adapter = AdsAdapter(itemWidth, vm)
        adapter.setShowSocialButtons(false)
        binding.recyclerView.adapter = adapter
        (binding.recyclerView.layoutManager as GridLayoutManager).spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (adapter.getItemViewType(position) == AdsAdapter.AD_TYPE) 1 else 3
//                    return adsAdapter.getItemViewType(position)
            }
        }

    }

    override fun onClickedListener() {
        binding.ibCall.setOnClickListener { openDialog() }
        binding.llFollowers.setOnClickListener {
            vm.onFollowerClickedListener(it, activityLauncher)
        }
        binding.llFollowings.setOnClickListener {
            vm.onFollowingClickedListener(it, activityLauncher)
        }

        binding.followButton.setOnClickListener {
            vm.onFollowClickedListener(this)
        }

    }

    override fun observerLiveData() {

        vm.userRes.observe(this) { user ->
            this.user = user
            initToolbar(user?._name ?: "")
            vm.followState.set(user?.isFollowing)
            vm.adsCount.set(user?.stats?.adsCount?.toString())
            vm.followersCount.set(user?.stats?.followersCount?.toString())
            vm.followingCount.set(user?.stats?.followingsCount?.toString())

            user?._name?.let {
                binding.usernameTextView.text = it
                binding.usernameTextView.isVisible = true
            }
            binding.mobileTextView.text = user.mobile

            user.avatarUrl?.let {
                Picasso.get().load(it).placeholder(R.drawable.avatar).into(binding.avatarImageView)
            }

            user.email?.let {
                binding.emailTextView.text = it
                binding.emailTextView.isVisible = true
            }

            user.bio?.let {
                binding.bioTextView.text = it
                binding.bioTextView.isVisible = true
            }

            user.location?.let {
                binding.ibUserLocation.isVisible = true
                binding.ibUserLocation.setOnClickListener {
                    Helper.goToMapDirection(this, user.location.coordinates.get(1).toString(), user.location.coordinates.get(0).toString(), user.location.address)
                }
            }
        }



        vm.adsRes.observe(this, Observer {
            adapter.submitList(it as PagedList<AdBanner>)
        })

        vm.getFooterState().observe(this, Observer {
            adapter.setState(it)
        })

        vm.getLayoutState().observe(this, Observer {
            vm.frameState.set(it)
        })

    }

    fun openDialog() {
        val list: ArrayList<SheetItem> = ArrayList<SheetItem>()
        if (::user.isInitialized) {
            if (!TextUtils.isEmpty(user.mobile)) {
                list.add(SheetItem(getString(R.string.call_item), Color.parseColor("#000000")))
                list.add(SheetItem(getString(R.string.msg_item), Color.parseColor("#000000")))
                list.add(SheetItem(getString(R.string.whatsapp_item), Color.parseColor("#000000")))
                list.add(SheetItem(getString(R.string.copy_item), Color.parseColor("#000000")))
                list.add(SheetItem(getString(R.string.chat_item), Color.parseColor("#000000")))
            }

            if (!TextUtils.isEmpty(user.email)) {
                list.add(SheetItem(getString(R.string.email_item), Color.parseColor("#000000")))
            }

        }

        val dialog = IOSSheetDialog.Builder(this).setCancelText(getString(R.string.cancel)).setData(list) { dialogInterface: DialogInterface?, i: Int ->
                if (!::user.isInitialized) return@setData
                if (TextUtils.isEmpty(user.mobile)) {
                    when (i) {
                        0 -> {
                            sendEmail(user.email)
                        }
                    }
                } else {
                    when (i) {
                        0 -> call(user.mobile)

                        1 -> {
                            sendTextMsg(user.mobile)
                        }

                        2 -> {
                            openWhatsapp(user.mobile)
                        }

                        3 -> {
                            copyNumber(user.mobile)
                        }

                        4 -> {
                            openChat(user.mobile)
                        }

                        5 -> {
                            sendEmail(user.email)
                        }

                    }
                }

            }.show()

        val params: ViewGroup.LayoutParams = dialog.window!!.attributes
        // Assign window properties to fill the parent
        // Assign window properties to fill the parent
        params.width = (resources.displayMetrics.widthPixels * 0.95).toInt()
        params.height = WindowManager.LayoutParams.WRAP_CONTENT

        dialog.window!!.attributes = params as WindowManager.LayoutParams

    }

    fun call(mobile: String) {
        if (TextUtils.isEmpty(mobile)) return
        Helper.callPhone(mobile, this)
    }

    fun sendTextMsg(mobile: String?) {
        if (TextUtils.isEmpty(mobile)) return
        Helper.sendSMS(mobile, this)

    }

    fun sendEmail(email: String?) {
        if (TextUtils.isEmpty(email)) return
        Helper.sendEmail(email, this)
    }

    fun openChat(mobile: String) {
        vm.onChatClickedListener(this, mobile)
    }

    fun openWhatsapp(mobile: String) {
        if (TextUtils.isEmpty(mobile)) return
        val url = "https://wa.me/${mobile}"
        Helper.openUrl(url, this)
    }

    fun copyNumber(mobile: String) {
        if (TextUtils.isEmpty(mobile)) return
        Helper.copyNumber(mobile, this)

    }

    val Activity.displayMetrics: DisplayMetrics
        get() {
            // display metrics is a structure describing general information
            // about a display, such as its size, density, and font scaling
            val displayMetrics = DisplayMetrics()

            if (Build.VERSION.SDK_INT >= 30) {
                display?.apply {
                    getRealMetrics(displayMetrics)
                }
            } else {
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
