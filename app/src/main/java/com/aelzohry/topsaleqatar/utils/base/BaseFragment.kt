package com.aelzohry.topsaleqatar.utils.base

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.aelzohry.topsaleqatar.BR
import com.aelzohry.topsaleqatar.R
import com.aelzohry.topsaleqatar.helper.Helper
import com.aelzohry.topsaleqatar.ui.auth.LoginActivity
import com.aelzohry.topsaleqatar.utils.Utils
import com.aelzohry.topsaleqatar.utils.extenions.*
import com.kaopiz.kprogresshud.KProgressHUD

abstract class BaseFragment<DB : ViewDataBinding, VM : BaseViewModel> : Fragment() {

    protected val activityLauncher = BetterActivityResult.registerActivityForResult(this)
    protected lateinit var binding: DB
    protected lateinit var vm: VM
    private var loading: KProgressHUD? = null
    protected lateinit var root: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        vm = getViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        performDataBinding(inflater, container)
        setupMapView(savedInstanceState)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

    private fun performDataBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = DataBindingUtil.inflate(inflater, getLayoutID(), container, false)
        root = binding.root
        binding.setVariable(BR.vm, vm)
        binding.executePendingBindings()
        lifecycle.addObserver(getViewModel())

    }

    private fun setupBaseLiveDataObserver() {
        vm.showToast.observe(viewLifecycleOwner, Observer {

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

        loading = requireActivity().showLoading()
        loading?.dismiss()
        vm.isLoading.observe(viewLifecycleOwner, {

            if (it) {
                loading?.show()
            } else {
                loading?.dismiss()
            }
        })

        vm.snackLogin.observe(viewLifecycleOwner, {

            if (it)
//                snackBar(R.string.youMustLogin, R.string.login) {
//                    startActivity(Intent(requireContext(), LoginActivity::class.java))
//
//                }
                if (activity != null) {
                    Utils.openLoginDialog(requireActivity().supportFragmentManager)
                }

        })
    }

    protected open fun setupMapView(savedInstanceState: Bundle?) {

    }

    fun Fragment.initToolbar(title: String, isViewBack: Boolean = true) {
        val toolbar = binding.root.findViewById<Toolbar>(R.id.toolbar)
        val mTitle = toolbar.findViewById<TextView>(R.id.toolbar_title)

        if (toolbar != null) {
            mTitle.setText(title)
            (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
            (requireActivity() as AppCompatActivity).title = ""
            if (isViewBack) (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(
                true
            )

            toolbar.setNavigationOnClickListener {
                Navigation.findNavController(it).popBackStack()
            }
        }
    }

}