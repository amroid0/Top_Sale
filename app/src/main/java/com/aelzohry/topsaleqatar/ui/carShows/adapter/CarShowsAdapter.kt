package com.aelzohry.topsaleqatar.ui.carShows.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aelzohry.topsaleqatar.R
import com.aelzohry.topsaleqatar.model.User

class CarShowsAdapter(val context: Context, var users: ArrayList<User>, val tapHandler: (user: User) -> Unit

) : RecyclerView.Adapter<CarShowViewHolder>() {

    private var IS_CHECKED_ARG: String? = "is_checked"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarShowViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_car_show, parent, false)
        return CarShowViewHolder(view)
    }

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: CarShowViewHolder, position: Int) {
        val user = users[position]
        holder.fillFrom(user)
        holder.itemView.setOnClickListener {
            toggleIsChecked(position)
        }

    }

    override fun onBindViewHolder(holder: CarShowViewHolder, position: Int, payloads: MutableList<Any>) {

        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            val bundle = payloads[0] as Bundle
            for (key in bundle.keySet()) {
                if (key == IS_CHECKED_ARG) {
                    val isChecked = bundle.getBoolean(IS_CHECKED_ARG)
                    holder.radioBtn.isChecked = isChecked
                }
            }
        }
    }

    fun toggleIsChecked(position: Int) {
        users.forEachIndexed { index, user ->
            if (index == position) {
                user.isChecked = !user.isChecked
                users.set(position, user)
                tapHandler(user)
            }else{
                user.isChecked = false
                users.set(position, user)
            }

            val bundle = Bundle()
            bundle.putBoolean(IS_CHECKED_ARG, user.isChecked)
            notifyItemChanged(position, bundle)
        }
    }
}