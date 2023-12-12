package com.aelzohry.topsaleqatar.ui.auth.loginDialog

import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.aelzohry.topsaleqatar.R
import com.aelzohry.topsaleqatar.helper.Helper
import com.aelzohry.topsaleqatar.helper.hideKeyboard
import com.aelzohry.topsaleqatar.repository.remote.RemoteRepository
import com.aelzohry.topsaleqatar.repository.remote.responses.LoginResponse
import com.aelzohry.topsaleqatar.utils.base.BaseViewModel
import retrofit2.Call

/*
* created by : ahmed mustafa
* email : ahmed.mustafa15996@gmail.com
*phone : +201025601465
*/
class LoginDialogViewModel : BaseViewModel() {

    private val repository = RemoteRepository()
    private var loginCall: Call<LoginResponse>? = null


    var verifyContainer = ObservableField(false)

    val etPhone = ObservableField("")
    var etCode = ObservableField("")

    var mobile = ""

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
                verifyContainer.set(true)
            }
        }
    }

    fun onVerifyClickedListener(v: View) {

        val code = etCode.get().toString()
        var mobile = etPhone.get() ?: ""


        mobile =
            if (!mobile.startsWith("201")) v.resources.getString(R.string.country_code) + mobile else "+$mobile"

        if (code.isEmpty()) {
            showToast.postValue(R.string.enter_verfication_cdoe)
            return
        }
        isLoading.postValue(true)
        loginCall = repository.login("", mobile, code, Helper.pushId) { response ->

            isLoading.postValue(false)
            showToast.postValue(response?.message ?: "Server Error")

            response?.data?.let {
                Helper.didSendPushIdToServer = it.didSavePushToken

                // persist authToken
                Helper.authToken = it.token
                Helper.userId = it.userId
                Helper.mobile = mobile
                Helper.restartApp(v.context)
            }
        }
    }

    fun onSendCodeAgainClickedListener(v: View) {
        isLoading.postValue(true)

        loginCall = repository.login("", mobile) { response ->
            isLoading.postValue(false)
            showToast.postValue(response?.message ?: "Server Error")
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        loginCall?.cancel()
    }
}