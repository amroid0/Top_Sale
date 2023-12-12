package com.aelzohry.topsaleqatar.ui.auth

import android.app.AlertDialog
import android.view.inputmethod.EditorInfo
import androidx.core.text.HtmlCompat
import androidx.databinding.DataBindingUtil
import com.aelzohry.topsaleqatar.R
import androidx.lifecycle.ViewModelProvider
import com.aelzohry.topsaleqatar.databinding.ActivityLoginBinding
import com.aelzohry.topsaleqatar.databinding.DialogTermsBinding
import com.aelzohry.topsaleqatar.ui.auth.vm.LoginViewModel
import com.aelzohry.topsaleqatar.utils.base.BaseActivity
import retrofit2.Call
import java.io.BufferedReader
import java.io.InputStreamReader


class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>() {

    var dialog: AlertDialog? = null

    override fun getLayoutID(): Int = R.layout.activity_login

    override fun getViewModel(): LoginViewModel =
        ViewModelProvider(this)[LoginViewModel::class.java]

    override fun setupUI() {


        binding.termsTextView.text =
            HtmlCompat.fromHtml(getString(R.string.terms, "<Br>"), HtmlCompat.FROM_HTML_MODE_LEGACY)

        setupTerms()
    }

    override fun onClickedListener() {
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
        dialog = AlertDialog.Builder(this)
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

}
