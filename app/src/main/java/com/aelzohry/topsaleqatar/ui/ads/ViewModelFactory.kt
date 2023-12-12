package com.aelzohry.topsaleqatar.ui.ads

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aelzohry.topsaleqatar.model.Category
import com.aelzohry.topsaleqatar.model.LocalStanderModel
import com.aelzohry.topsaleqatar.ui.ads.filter.FilterViewModel
import com.aelzohry.topsaleqatar.ui.ads.filter.NewFilterViewModel

/*
* created by : ahmed mustafa
* email : ahmed.mustafa15996@gmail.com
*phone : +201025601465
*/
class ViewModelFactory(
    private val category: Category?,
    private val subCategory: LocalStanderModel? = null,
    private val loadCategory: Boolean = true,
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return when {
            modelClass.isAssignableFrom(FilterViewModel::class.java) -> FilterViewModel(category = category, loadCategory = loadCategory) as T
            modelClass.isAssignableFrom(NewFilterViewModel::class.java) -> NewFilterViewModel(category = category, loadCategory = loadCategory) as T
            modelClass.isAssignableFrom(AdsViewModel::class.java) -> AdsViewModel(category = category, subCategory) as T
            else -> super.create(modelClass)
        }

    }
}