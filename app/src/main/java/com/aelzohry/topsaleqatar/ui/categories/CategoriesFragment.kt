package com.aelzohry.topsaleqatar.ui.categories

import android.content.Context
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.aelzohry.topsaleqatar.R
import com.aelzohry.topsaleqatar.databinding.FragmentCategoriesBinding
import com.aelzohry.topsaleqatar.databinding.ViewHolderCategoryBinding
import com.aelzohry.topsaleqatar.databinding.ViewHolderSubcategoryBinding
import com.aelzohry.topsaleqatar.helper.Helper
import com.aelzohry.topsaleqatar.model.Category
import com.aelzohry.topsaleqatar.repository.remote.RemoteRepository
import com.aelzohry.topsaleqatar.repository.remote.responses.CategoryData
import com.aelzohry.topsaleqatar.ui.messages.MessagesFragment
import com.aelzohry.topsaleqatar.ui.notification.NotificationActivity
import com.aelzohry.topsaleqatar.utils.base.BaseAdapter
import com.aelzohry.topsaleqatar.utils.base.BaseFragment
import com.aelzohry.topsaleqatar.utils.extenions.setVisible
import com.onesignal.OneSignal

class CategoriesFragment : BaseFragment<FragmentCategoriesBinding, CategoriesViewModel>() {

    private lateinit var mListener: MessagesFragment.Listener

    override fun getLayoutID(): Int = R.layout.fragment_categories

    override fun getViewModel(): CategoriesViewModel = ViewModelProvider(this)[CategoriesViewModel::class.java]

    override fun setupUI() {
        setOneSignalListener()
    }

    override fun onClickedListener() {
        binding.ibBack.setOnClickListener {
            Navigation.findNavController(it).popBackStack()
        }

        binding.ibNotification.setOnClickListener {
            startActivity(NotificationActivity.newInstance(requireContext()))
        }

//        binding.etSearch.setOnEditorActionListener { v, actionId, event ->
//            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                vm.onSearchClickedListener(v)
//                return@setOnEditorActionListener true
//            }
//            return@setOnEditorActionListener false
//        }
    }

    override fun observerLiveData() {
        vm.categoriesRes.observe(this, Observer {
            binding.recyclerView.adapter = BaseAdapter<CategoryData, ViewHolderCategoryBinding>(R.layout.view_holder_category, vm, it) { bind, model, position, adapter ->

                val list = model.categories
                bind.rvSubCats.adapter = BaseAdapter<Category, ViewHolderSubcategoryBinding>(R.layout.view_holder_subcategory, vm, list) { subBind, subModel, subPosition, _ ->
                    subBind.root.setOnClickListener {
                        vm.onCatClickedListener(it, subModel)
                    }
                }
            }
        })

        vm.notificationNumberRes.observe(this) { number ->
            showOrHideBadge(number)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            mListener = context as MessagesFragment.Listener
        } catch (e: ClassCastException) {
            throw ClassCastException(
                context.toString()
                        + "must implement the SendMessageListener interface"
            )
        }
    }

    fun setOneSignalListener() {
        OneSignal.setNotificationWillShowInForegroundHandler {
            loadNumberOfUnSeenNotification()
        }
    }

    fun loadNumberOfUnSeenNotification() {
        val repository = RemoteRepository()
        Helper.getUnSeenNotificationNumber(repository, object : Helper.Listener {
            override fun passUnSeenNotificationNumber(notificationNumber: Int,messagesNumber: Int) {
                showOrHideBadge(notificationNumber)
                if (::mListener.isInitialized)
                    mListener.onBadgeChange(notificationNumber)
            }
        })
    }

    private fun showOrHideBadge(number: Int) {
        if (::mListener.isInitialized)
            mListener.onBadgeChange(number)
        binding.tvNotificationBadge.text = number.toString()
        binding.tvNotificationBadge.setVisible(number != 0)
    }


}
