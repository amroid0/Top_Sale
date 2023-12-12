package com.aelzohry.topsaleqatar.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.aelzohry.topsaleqatar.R
import com.aelzohry.topsaleqatar.databinding.ListItemBinding
import com.aelzohry.topsaleqatar.model.LocalStanderModel
import com.aelzohry.topsaleqatar.utils.base.BaseViewHolder

/*
* created by : ahmed mustafa
* email : ahmed.mustafa15996@gmail.com
*phone : +201025601465
*/
class DialogListAdapter(
    private val list: List<LocalStanderModel>,
    private val isMulti: Boolean = false,
    private val onItemClickedListener: (modelLocal: LocalStanderModel, i: Int) -> Unit
) :
    RecyclerView.Adapter<BaseViewHolder<LocalStanderModel, ListItemBinding>>() {

//    private var listFiltered: List<StanderModel> = list

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<LocalStanderModel, ListItemBinding> = BaseViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.list_item, parent, false
        ), null
    )

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(
        holder: BaseViewHolder<LocalStanderModel, ListItemBinding>,
        position: Int
    ) {

        holder.bind(list[position])

        if (isMulti) {
            holder.binding.cbCheck.visibility = View.VISIBLE
        }else{
            holder.binding.cbCheck.visibility = View.INVISIBLE

        }
        holder.binding.root.setOnClickListener {
            if (isMulti) {
                list[position].is_checked = !list[position].is_checked
            }

            onItemClickedListener(list[position], position)
        }
    }

    fun getSelectedList(): ArrayList<LocalStanderModel> {
        val sl = ArrayList<LocalStanderModel>()
        list.forEach {
            if (it.isChecked)
                sl.add(it)
        }
        return sl
    }

//    override fun getFilter(): Filter {
//        return object : Filter() {
//            override fun performFiltering(charSequence: CharSequence?): FilterResults {
//
//                val charString = charSequence.toString()
//                listFiltered = if (charString.isEmpty()) {
//                    list
//                } else {
//                    val filtered = ArrayList<StanderModel>()
//                    list.forEach {
//                        if (it.name.contains(charString, true)) filtered.add(it)
//                    }
//                    filtered
//                }
//
//                val filterResults = FilterResults()
//                filterResults.values = listFiltered
//                return filterResults
//
//            }
//
//            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
//
//                listFiltered = results?.values as ArrayList<StanderModel>
//                notifyDataSetChanged()
//
//            }
//
//        }
//    }

}