package com.aelzohry.topsaleqatar.ui.messages

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.aelzohry.topsaleqatar.helper.Helper
import com.aelzohry.topsaleqatar.model.Ad
import com.aelzohry.topsaleqatar.repository.Repository
import com.aelzohry.topsaleqatar.repository.remote.RemoteRepository
import com.aelzohry.topsaleqatar.utils.base.BaseViewModel

/*
* created by : ahmed mustafa
* email : ahmed.mustafa15996@gmail.com
*phone : +201025601465
*/
class MessagesViewModel : BaseViewModel() {

    val typeState = ObservableField(MessagesFragment.Mode.CHAT)

}