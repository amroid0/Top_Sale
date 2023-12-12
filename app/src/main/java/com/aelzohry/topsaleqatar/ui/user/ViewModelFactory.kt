package com.aelzohry.topsaleqatar.ui.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aelzohry.topsaleqatar.model.User

/*
* created by : ahmed mustafa
* email : ahmed.mustafa15996@gmail.com
*phone : +201025601465
*/
class ViewModelFactory(private val user: User?, private val userId: String?) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = UserViewModel(user = user, userId = userId) as T
}