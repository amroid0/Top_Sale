package com.aelzohry.topsaleqatar.utils.base

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.aelzohry.topsaleqatar.BR
import com.aelzohry.topsaleqatar.helper.Helper
import com.aelzohry.topsaleqatar.utils.extenions.showLoading
import com.aelzohry.topsaleqatar.utils.extenions.showToast
import com.kaopiz.kprogresshud.KProgressHUD

/*
* created by : ahmed mustafa
* email : ahmed.mustafa15996@gmail.com
*phone : +201025601465
*/
abstract class BaseDialogFragment<DB : ViewDataBinding, VM : BaseViewModel> : DialogFragment() {


    protected lateinit var binding: DB
    protected lateinit var vm: VM
    private var loading: KProgressHUD? = null
    protected lateinit var root: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        setupBaseLiveDataObserver()
        setupUI()
        onClickedListener()
        observerLiveData()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
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
        vm.isLoading.observe(viewLifecycleOwner, Observer {

            if (it) {
                loading?.show()
            } else {
                loading?.dismiss()
            }
        })

        vm.snackLogin.observe(viewLifecycleOwner, Observer {

//            if (it)
//                snackBar(R.string.youMustLogin, R.string.login) {
//                    val bottomSheet =
//                        LoginBottomSheet.navToLoginBottomSheet()
//                    bottomSheet.show(childFragmentManager, bottomSheet.tag)
//
//                }
        })
    }

    override fun onResume() {

        AppCompatDelegate.setDefaultNightMode(
            if (Helper.isNightMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )


        val params: ViewGroup.LayoutParams = dialog?.window!!.attributes
        // Assign window properties to fill the parent
        // Assign window properties to fill the parent
        params.width = (resources.displayMetrics.widthPixels * 0.90).toInt()
        params.height = WindowManager.LayoutParams.WRAP_CONTENT

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        super.onResume()

    }


}