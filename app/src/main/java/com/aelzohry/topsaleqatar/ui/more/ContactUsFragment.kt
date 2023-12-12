package com.aelzohry.topsaleqatar.ui.more

import androidx.lifecycle.ViewModelProvider
import com.aelzohry.topsaleqatar.R
import com.aelzohry.topsaleqatar.databinding.FragmentContactUsBinding
import com.aelzohry.topsaleqatar.helper.Helper
import com.aelzohry.topsaleqatar.utils.base.BaseActivity
import com.aelzohry.topsaleqatar.utils.base.BaseViewModel

class ContactUsFragment : BaseActivity<FragmentContactUsBinding, BaseViewModel>() {

    override fun getLayoutID(): Int = R.layout.fragment_contact_us

    override fun getViewModel(): BaseViewModel = ViewModelProvider(this)[BaseViewModel::class.java]

    override fun setupUI() {
        initToolbar(getString(R.string.contact_us))
    }

    override fun onClickedListener() {
        binding.officeTextView.setOnClickListener {
//            Helper.callPhone("66692959", this)
            Helper.newWhatsAppIntent(this, packageManager, "66692959")
        }

        binding.infoEmailTextView.setOnClickListener {
            Helper.sendEmail("info@topsale.email", this)
        }
    }

    override fun observerLiveData() {

    }

}
