package com.aelzohry.topsaleqatar.utils.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

open class BaseAdapter<M, B : ViewDataBinding>(
    @LayoutRes private var layoutId: Int,
    protected var viewModel: BaseViewModel?,
    newList: List<M>? = null,
    private var bind: (B, model: M, position: Int, adapter: BaseAdapter<M, B>) -> Unit = { _, _, _, _ -> }
) :
    RecyclerView.Adapter<BaseViewHolder<M, B>>() {

    protected var list = ArrayList<M>()

    init {
        newList?.let {
            list.addAll(it)
        }
    }

    fun addList(newList: List<M>?) {
        newList?.let {
            list.addAll(it)
            notifyItemRangeInserted(itemCount, newList.size)
        }
    }

    fun addItem(model: M?, isFirst: Boolean = false) {
        model?.let {
            if (isFirst) list.add(0, model) else list.add(model)
            notifyItemInserted(if (isFirst) 0 else itemCount)
        }
    }

    fun removeItem(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, list.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<M, B> =
        BaseViewHolder(
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), layoutId, parent, false),
            viewModel
        )

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: BaseViewHolder<M, B>, position: Int) {
        holder.bind(list[position])
        bind(holder.binding, list[position], position, this)
    }
}