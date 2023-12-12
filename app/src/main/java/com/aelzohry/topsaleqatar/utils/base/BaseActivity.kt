package com.aelzohry.topsaleqatar.utils.base

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.aelzohry.topsaleqatar.BR
import com.aelzohry.topsaleqatar.R
import com.aelzohry.topsaleqatar.helper.Helper
import com.aelzohry.topsaleqatar.utils.Localization
import com.aelzohry.topsaleqatar.utils.Utils
import com.aelzohry.topsaleqatar.utils.extenions.showLoading
import com.aelzohry.topsaleqatar.utils.extenions.showToast
import com.kaopiz.kprogresshud.KProgressHUD

abstract class BaseActivity<DB : ViewDataBinding, VM : BaseViewModel> : AppCompatActivity() {


    protected val activityLauncher = BetterActivityResult.registerActivityForResult(this)
    protected val permissionLauncher = PermissionResult.registerActivityForResult(this)

    protected lateinit var binding: DB
    protected lateinit var vm: VM
    private var loading: KProgressHUD? = null


    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        Localization.setLanguage(this, Helper.languageCode)
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        resetTitle()
        if (isFullscreen()) {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        configScreen()
        performDataBinding()
        setupBaseLiveDataObserver()
        setupUI()
        onClickedListener()
        observerLiveData()
    }

    override fun onResume() {
        super.onResume()

        AppCompatDelegate.setDefaultNightMode(
            if (Helper.isNightMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate
                .MODE_NIGHT_NO
        )
    }

    /**
     * @return layout resource id
     */
    @LayoutRes
    protected abstract fun getLayoutID(): Int

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    protected abstract fun getViewModel(): VM

    /**
     * Override for set up ui
     *
     * @return view model instance
     */

    protected abstract fun setupUI()

    /**
     * Override for handel actions
     *
     * @return view model instance
     */
    protected abstract fun onClickedListener()

    /**
     * Override for liveData listener
     *
     * @return view model instance
     */
    protected abstract fun observerLiveData()

    protected open fun isFullscreen(): Boolean = false

    private fun performDataBinding() {
        binding = DataBindingUtil.setContentView(this, getLayoutID())
        vm = getViewModel()
        binding.setVariable(BR.vm, vm)
        binding.executePendingBindings()
        lifecycle.addObserver(getViewModel())
    }

    private fun setupBaseLiveDataObserver() {
        vm.showToast.observe(this, Observer {

            if (it == null)
                return@Observer

            if (it is String) {
                showToast(it)
                return@Observer
            }

            if (it is Int) {
                showToast(it)
                return@Observer
            }
        })

        loading = showLoading()
        try {

            vm.isLoading.observe(this, Observer {


                if (!isFinishing() && loading != null && !isDestroyed) {
                    if (it) {
                           loading?.show()

                    } else {
                        if (loading!!.isShowing) {
                            loading?.dismiss()
                        }
                    }
                }


            })
        } catch (ex: Exception) {

        }
        vm.snackLogin.observe(this, Observer {

            if (it)
//                snackBar(R.string.youMustLogin, R.string.login) {
//                    startActivity(Intent(this, LoginActivity::class.java))
//                }

                    Utils.openLoginDialog(supportFragmentManager)

        })
    }

    private fun resetTitle() {

        try {
            var label =
                packageManager.getActivityInfo(componentName, PackageManager.GET_META_DATA).labelRes
            if (label != 0) {
                setTitle(label)
            }
        } catch (ex: Exception) {

        }
    }

    protected open fun configScreen() {}

    override fun onDestroy() {
        super.onDestroy()
        try {
            if (loading != null && loading!!.isShowing) {
                loading!!.dismiss();
            }
            lifecycle.removeObserver(vm)
        } catch (ex: Exception) {

        }
    }

    fun AppCompatActivity.initToolbar(title: String, isViewBack: Boolean = true) {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val mTitle = toolbar.findViewById<TextView>(R.id.toolbar_title)

        if (toolbar != null) {
            mTitle.setText(title)
            setSupportActionBar(toolbar)
            supportActionBar?.title = ""
            if (isViewBack) supportActionBar?.setDisplayHomeAsUpEnabled(true)

            toolbar.setNavigationOnClickListener {
                onBackPressed()
            }
        }
    }


}