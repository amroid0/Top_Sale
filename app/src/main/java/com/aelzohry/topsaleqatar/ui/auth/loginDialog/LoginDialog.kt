package com.aelzohry.topsaleqatar.ui.auth.loginDialog

import android.app.AlertDialog
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.core.text.HtmlCompat
import androidx.databinding.DataBindingUtil
import com.aelzohry.topsaleqatar.R
import androidx.lifecycle.ViewModelProvider
import com.aelzohry.topsaleqatar.databinding.ActivityLoginBinding
import com.aelzohry.topsaleqatar.databinding.DialogTermsBinding
import com.aelzohry.topsaleqatar.databinding.FragmentDialogLoginBinding
import com.aelzohry.topsaleqatar.ui.auth.vm.LoginViewModel
import com.aelzohry.topsaleqatar.utils.base.BaseBottomSheet
import java.io.BufferedReader
import java.io.InputStreamReader


class LoginDialog : BaseBottomSheet<FragmentDialogLoginBinding, LoginDialogViewModel>() {

    var dialog: AlertDialog? = null

    override fun getLayoutID(): Int = R.layout.fragment_dialog_login

    override fun getViewModel(): LoginDialogViewModel =
        ViewModelProvider(this)[LoginDialogViewModel::class.java]


    companion object {
        fun newInstance(): LoginDialog {
            val fragment = LoginDialog()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    override fun setupUI() {

        binding.termsTextView.text =
            HtmlCompat.fromHtml(getString(R.string.terms, "<Br>"), HtmlCompat.FROM_HTML_MODE_LEGACY)

        setupTerms()
    }

    override fun onClickedListener() {
        binding.ibClose.setOnClickListener { dismiss() }

        binding.termsLayout.setOnClickListener {
            dialog?.show()
        }

        binding.etPhone.setOnEditorActionListener { v, actionId, event ->

            if (actionId == EditorInfo.IME_ACTION_GO) {
                vm.onLoginBtnClickedListener(v)
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener true
        }
    }

    override fun observerLiveData() {

    }

    private fun setupTerms() {

        var dialogBinding = DataBindingUtil.inflate<DialogTermsBinding>(
            layoutInflater,
            R.layout.dialog_terms,
            null,
            false
        )
        dialog = AlertDialog.Builder(activity)
            .setView(dialogBinding.root)
            .setNegativeButton(R.string.ok) { _, _ ->

            }
            .create()
        binding.executePendingBindings()

        try {
//            val inS = resources.openRawResource(R.raw.terms)
//            val b = ByteArray(inS.available())
//            inS.read(b)

            val inputStream = resources.openRawResource(R.raw.terms)
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            var eachline = bufferedReader.readLine()
            var txt = StringBuffer()
            while (eachline != null) {
                txt.append(eachline)
                eachline = bufferedReader.readLine()
            }
            dialogBinding?.termsTextView?.text = txt.toString()
        } catch (e: Exception) {
            dialogBinding?.termsTextView?.text = getString(R.string.error_terms)
        }
    }

    override fun isFullHeight(): Boolean {
        return true
    }

}
