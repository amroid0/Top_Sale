package com.aelzohry.topsaleqatar.ui.auth.vm

import android.content.Intent
import android.view.View
import androidx.constraintlayout.motion.widget.TransitionBuilder.validate
import androidx.databinding.ObservableField
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.aelzohry.topsaleqatar.R
import com.aelzohry.topsaleqatar.helper.Helper
import com.aelzohry.topsaleqatar.helper.hideKeyboard
import com.aelzohry.topsaleqatar.repository.remote.RemoteRepository
import com.aelzohry.topsaleqatar.repository.remote.responses.LoginResponse
import com.aelzohry.topsaleqatar.ui.auth.VerifyActivity
import com.aelzohry.topsaleqatar.utils.base.BaseViewModel
import retrofit2.Call

/*
* created by : ahmed mustafa
* email : ahmed.mustafa15996@gmail.com
*phone : +201025601465
*/
class LoginViewModel : BaseViewModel() {

    private val repository = RemoteRepository()
    private var loginCall: Call<LoginResponse>? = null


    val etPhone = ObservableField("")


    fun onLoginBtnClickedListener(v: View) {

        var mobile = etPhone.get() ?: ""

        if (mobile.isEmpty()) {

            showToast.postValue(R.string.you_must_enter_phone_number)
            return

        }
        v.hideKeyboard()

        mobile =
            if (!mobile.startsWith("201")) v.resources.getString(R.string.country_code) + mobile
            else "+$mobile"


        isLoading.postValue(true)
        loginCall = repository.login("", mobile) { response ->
            isLoading.postValue(false)
            showToast.postValue(response?.message ?: "Server Error")

            if (response?.success == true) {
                v.context.startActivity(
                    Intent(v.context, VerifyActivity::class.java)
                        .putExtra(VerifyActivity.NAME, "")
                        .putExtra(VerifyActivity.MOBILE, mobile)
                )
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        loginCall?.cancel()
    }
}