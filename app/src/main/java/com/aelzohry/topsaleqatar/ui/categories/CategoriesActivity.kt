package com.aelzohry.topsaleqatar.ui.categories

import android.view.inputmethod.EditorInfo
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.aelzohry.topsaleqatar.R
import com.aelzohry.topsaleqatar.databinding.ActivityCategoriesBinding
import com.aelzohry.topsaleqatar.databinding.ViewHolderCategoryBinding
import com.aelzohry.topsaleqatar.databinding.ViewHolderSubcategoryBinding
import com.aelzohry.topsaleqatar.model.Category
import com.aelzohry.topsaleqatar.repository.remote.responses.CategoryData
import com.aelzohry.topsaleqatar.utils.base.BaseActivity
import com.aelzohry.topsaleqatar.utils.base.BaseAdapter

class CategoriesActivity : BaseActivity<ActivityCategoriesBinding, CategoriesViewModel>() {

    override fun getLayoutID(): Int = R.layout.activity_categories

    override fun getViewModel(): CategoriesViewModel = ViewModelProvider(this)[CategoriesViewModel::class.java]

    override fun setupUI() {
//        initToolbar(getString(R.string.categories))
    }

    override fun onClickedListener() {

        binding.imgBack.setOnClickListener { finish() }

        binding.etSearch.setOnEditorActionListener { v, actionId, event ->

            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                vm.onSearchClickedListener(v)
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
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
    }

}
