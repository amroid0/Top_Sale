package com.aelzohry.topsaleqatar.ui

import androidx.lifecycle.ViewModelProvider
import com.aelzohry.topsaleqatar.R
import com.aelzohry.topsaleqatar.databinding.ActivityTermsBinding
import com.aelzohry.topsaleqatar.utils.base.BaseActivity
import com.aelzohry.topsaleqatar.utils.base.BaseViewModel


class TermsActivity : BaseActivity<ActivityTermsBinding, BaseViewModel>() {

    override fun getLayoutID(): Int = R.layout.activity_terms

    override fun getViewModel(): BaseViewModel = ViewModelProvider(this)[BaseViewModel::class.java]

    override fun setupUI() {
        setFinishOnTouchOutside(true)
        title = "Terms of Use"

        binding.termsTextView.setOnClickListener {
            finish()
        }

        try {
            val res = resources
            val in_s = res.openRawResource(R.raw.terms)
            val b = ByteArray(in_s.available())
            in_s.read(b)
            binding.termsTextView.setText(String(b))
        } catch (e: Exception) {
            binding.termsTextView.setText("Error: can't show terms.")
        }
    }

    override fun onClickedListener() {

    }

    override fun observerLiveData() {

    }

}