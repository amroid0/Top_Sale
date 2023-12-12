package com.aelzohry.topsaleqatar.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/*
* created by : ahmed mustafa
* email : ahmed.mustafa15996@gmail.com
*phone : +201025601465
*/
class ViewModelFactory(private val txt: String?) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return when {
            modelClass.isAssignableFrom(SearchViewModel::class.java) -> SearchViewModel(txt = txt) as T
            else -> super.create(modelClass)
        }

    }
}