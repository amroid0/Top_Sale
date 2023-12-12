package com.aelzohry.topsaleqatar.utils.base.baseModel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.viewpager.widget.PagerAdapter
import com.aelzohry.topsaleqatar.BR

/*
* created by : ahmed mustafa
* email : ahmed.mustafa15996@gmail.com
*phone : +201025601465
*/
class BasePagerAdapter<M, B : ViewDataBinding>(
    @LayoutRes private val layoutId: Int,
    private val list: List<M>,
    private var bind: (bind: B, model: M, position: Int,adapter: BasePagerAdapter<M, B>) -> Unit? = { _, _, _, _ -> }
) :
    PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val binding = DataBindingUtil.inflate<B>(
            LayoutInflater.from(container.context),
            layoutId,
            container,
            false
        )

        binding.setVariable(BR.model, list[position])
        binding.executePendingBindings()
        bind(binding,list[position],position,this)
        container.addView(binding.root)
        return binding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

    override fun getCount(): Int = list.size
}