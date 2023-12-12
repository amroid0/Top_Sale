package com.aelzohry.topsaleqatar.ui.new_ad.categorisDialog.view

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.aelzohry.topsaleqatar.R
import com.aelzohry.topsaleqatar.databinding.FragmentCategoriesDialogBinding
import com.aelzohry.topsaleqatar.databinding.ItemSubCategoryBinding
import com.aelzohry.topsaleqatar.databinding.ViewHolderCategoryBinding
import com.aelzohry.topsaleqatar.model.Category
import com.aelzohry.topsaleqatar.repository.remote.responses.CategoryData
import com.aelzohry.topsaleqatar.ui.categories.CategoriesViewModel
import com.aelzohry.topsaleqatar.utils.base.BaseAdapter
import com.aelzohry.topsaleqatar.utils.base.BaseBottomSheet

class CategoriesDialogFragment : BaseBottomSheet<FragmentCategoriesDialogBinding, CategoriesViewModel>() {

    private lateinit var mListener: Listener

    private var listData: ArrayList<CategoryData> = arrayListOf()

    fun setListener(listener: Listener) {
        mListener = listener
    }

    companion object {
        fun newInstance(list: ArrayList<CategoryData>): CategoriesDialogFragment {
            val fragment = CategoriesDialogFragment()
            val args = Bundle()
            args.putParcelableArrayList("test", list)
            fragment.arguments = args
            return fragment
        }
    }

    override fun getLayoutID(): Int = R.layout.fragment_categories_dialog

    override fun getViewModel(): CategoriesViewModel = ViewModelProvider(this)[CategoriesViewModel::class.java]

    override fun setupUI() {
        if (arguments != null) {
            listData = requireArguments().getParcelableArrayList("test")!!
        }
        Log.e("test_data", "TEST " + listData.size)

        binding.ibClose.setOnClickListener {
            dismiss()
        }
    }

    override fun observerLiveData() {
        vm.categoriesRes.value = listData
//        vm.categoriesRes.observe(this, Observer {
        binding.recyclerView.adapter = BaseAdapter<CategoryData, ViewHolderCategoryBinding>(R.layout.view_holder_category, vm, listData) { bind, model, position, adapter ->

            val list = model.categories
            bind.rvSubCats.adapter = BaseAdapter<Category, ItemSubCategoryBinding>(R.layout.item_sub_category, vm, list) { subBind, subModel, subPosition, _ ->
                subBind.root.setOnClickListener {
                    mListener.onItemSelected(subModel)
                    Log.e("test", subModel._id + subModel.title.localized)
                }
            }
        }
//        }
//        )
    }

    override fun onClickedListener() {

    }

    override fun isFullHeight(): Boolean {
        return false
    }

    interface Listener {
        fun onItemSelected(itemSelected: Category)
    }


}