package com.aelzohry.topsaleqatar.ui.categories

import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import com.aelzohry.topsaleqatar.helper.Constants.FROM_CATEGORY
import com.aelzohry.topsaleqatar.helper.Constants.FROM_OTHERS
import com.aelzohry.topsaleqatar.helper.Helper
import com.aelzohry.topsaleqatar.helper.hideKeyboard
import com.aelzohry.topsaleqatar.model.Category
import com.aelzohry.topsaleqatar.model.LocalStanderModel
import com.aelzohry.topsaleqatar.repository.Repository
import com.aelzohry.topsaleqatar.repository.remote.RemoteRepository
import com.aelzohry.topsaleqatar.repository.remote.responses.Categories
import com.aelzohry.topsaleqatar.repository.remote.responses.CategoriesGroupd
import com.aelzohry.topsaleqatar.repository.remote.responses.CategoriesResponse
import com.aelzohry.topsaleqatar.repository.remote.responses.CategoryData
import com.aelzohry.topsaleqatar.ui.ads.AdsFragment
import com.aelzohry.topsaleqatar.ui.search.SearchFragment
import com.aelzohry.topsaleqatar.utils.CustomFrame
import com.aelzohry.topsaleqatar.utils.base.BaseViewModel
import retrofit2.Call

/*
* created by : ahmed mustafa
* email : ahmed.mustafa15996@gmail.com
*phone : +201025601465
*/
class CategoriesViewModel : BaseViewModel() {

    private lateinit var repository: Repository

    var call: Call<CategoriesGroupd>? = null
    var categoriesRes = MutableLiveData<List<CategoryData>>()
    var etSearch = ObservableField("")

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun loadData() {
        loadData(false)

    }

    private fun loadData(isSwipe: Boolean = false) {
        if (isSwipe) swipeRefresh.set(true)
        frameState.set(CustomFrame.FrameState.PROGRESS)
        repository = RemoteRepository()
        call = repository.getCategoriesGroupd { response ->
            response?.data?.let { cats ->
                categoriesRes.postValue(cats.sortedBy { it.order })
            }
            frameState.set(CustomFrame.FrameState.LAYOUT)
            if (isSwipe) swipeRefresh.set(false)
        }

        Helper.getUnSeenNotificationNumber(repository, object : Helper.Listener {
            override fun passUnSeenNotificationNumber(notificationNumber: Int, messagesNumber: Int) {
                notificationNumberRes.postValue(notificationNumber)
            }
        })

    }

    fun onSwipeRefreshListener() {
        loadData(true)
    }

    fun onCatClickedListener(v: View, model: Category) {

        AdsFragment.newInstance(v.context,FROM_CATEGORY, model)
        /*
             val adsFragment = AdsFragment.newInstance(category)
        (activity as MainActivity).pushFragment(adsFragment)

        * */

    }

    fun onSearchClickedListener(v: View) {

        val txt = etSearch.get() ?: ""
        if (txt.isEmpty())
            return

        etSearch.set("")
        v.hideKeyboard()
        SearchFragment.newInstance(v.context, txt)

    }

    fun onItemCatSelectedListener(v: View, model: Category) {


    }

    fun onSubCatClickedListener(v: View, model: Category, subCategory: LocalStanderModel) {

        AdsFragment.newInstance(v.context,FROM_OTHERS, model, subCategory)
    }

}