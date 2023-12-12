package com.aelzohry.topsaleqatar.utils.base

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.aelzohry.topsaleqatar.BR

class BaseViewHolder<M, B : ViewDataBinding>(var binding: B, private var vm: BaseViewModel?) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(model: M?) {
        vm?.let {
            binding.setVariable(BR.vm, vm)
        }
        binding.setVariable(BR.i, adapterPosition)
        binding.setVariable(BR.i, adapterPosition)
        binding.setVariable(BR.model, model)
        binding.executePendingBindings()
    }
}