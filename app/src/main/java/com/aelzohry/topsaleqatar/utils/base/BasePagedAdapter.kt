package com.aelzohry.topsaleqatar.utils.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.aelzohry.topsaleqatar.R

open class BasePagedAdapter<M, B : ViewDataBinding>(

    @LayoutRes private val layoutRes: Int,
    private var vm: BaseViewModel? = null,
    val itemsTheSame: (oldItem: M, newItem: M) -> Boolean,
    val contentsTheSame: (oldItem: M, newItem: M) -> Boolean,
    private var bind: (bind: B, model: M, position: Int, adapter: BasePagedAdapter<M, B>) -> Unit? = { _, _, _, _ -> }
) : PagedListAdapter<M, RecyclerView.ViewHolder>(object : DiffUtil.ItemCallback<M>() {

    override fun areItemsTheSame(oldItem: M, newItem: M): Boolean = itemsTheSame(oldItem, newItem)

    override fun areContentsTheSame(oldItem: M, newItem: M): Boolean =
        contentsTheSame(oldItem, newItem)


}) {

    private var state = false

    override fun getItemViewType(position: Int): Int {
        return if (position < super.getItemCount()) DATA_TYPE else LOAD_TYPE
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasFooter()) 1 else 0
    }

    private fun hasFooter(): Boolean {
        return super.getItemCount() != 0 && state
    }

    fun setState(state: Boolean) {
        this.state = state
        notifyItemChanged(super.getItemCount())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        var inflater = LayoutInflater.from(parent.context)

        return if (viewType == DATA_TYPE) BaseViewHolder<M, B>(
            DataBindingUtil.inflate<B>(
                inflater,
                layoutRes,
                parent,
                false
            ), vm
        )
        else LoadVH(inflater.inflate(R.layout.load_item, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            DATA_TYPE -> {

                getItem(position)?.let {
                    (holder as BaseViewHolder<M, B>).bind(it)
                    bind(holder.binding, it, position, this)
                }
            }
        }
    }

    companion object {
        const val DATA_TYPE = 1
        const val LOAD_TYPE = 3
    }

    class LoadVH(itemView: View) : RecyclerView.ViewHolder(itemView)
}