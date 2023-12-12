package com.aelzohry.topsaleqatar.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import com.aelzohry.topsaleqatar.R
import androidx.lifecycle.ViewModelProvider
import com.aelzohry.topsaleqatar.databinding.ActivityVerifyBinding
import com.aelzohry.topsaleqatar.ui.auth.vm.VerifyViewModel
import com.aelzohry.topsaleqatar.utils.base.BaseActivity


class VerifyActivity : BaseActivity<ActivityVerifyBinding, VerifyViewModel>() {

    companion object {
        const val NAME = "NAME"
        const val MOBILE = "MOBILE"
    }

    lateinit var name: String
    lateinit var mobile: String

    override fun getLayoutID(): Int = R.layout.activity_verify

    override fun getViewModel(): VerifyViewModel =
        ViewModelProvider(this)[VerifyViewModel::class.java]

    override fun setupUI() {

        initToolbar(getString(R.string.verification_code))
        name = intent.getStringExtra(NAME)!!
        vm.mobile = intent.getStringExtra(MOBILE)!!

    }

    override fun onClickedListener() {

        binding.etPhone.setOnEditorActionListener { v, actionId, event ->

            if (actionId == EditorInfo.IME_ACTION_GO) {
                vm.onVerifyClickedListener(v)
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener true
        }
    }

    override fun observerLiveData() {

    }
}
