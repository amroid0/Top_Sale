package com.aelzohry.topsaleqatar.ui.auth.vm

import android.util.Log
import android.view.View
import androidx.databinding.ObservableField
import com.aelzohry.topsaleqatar.R
import com.aelzohry.topsaleqatar.helper.Helper
import com.aelzohry.topsaleqatar.repository.Repository
import com.aelzohry.topsaleqatar.repository.remote.RemoteRepository
import com.aelzohry.topsaleqatar.repository.remote.responses.LoginResponse
import com.aelzohry.topsaleqatar.utils.base.BaseViewModel
import retrofit2.Call

/*
* created by : ahmed mustafa
* email : ahmed.mustafa15996@gmail.com
*phone : +201025601465
*/
class VerifyViewModel : BaseViewModel() {

    private var repository: Repository = RemoteRepository()
    private var loginCall: Call<LoginResponse>? = null

    var etCode = ObservableField("")
    var mobile = ""

    fun onVerifyClickedListener(v: View) {

        val code = etCode.get().toString()

        if (code.isEmpty()){
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
}