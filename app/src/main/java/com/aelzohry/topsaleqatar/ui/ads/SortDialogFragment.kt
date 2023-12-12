package com.aelzohry.topsaleqatar.ui.ads

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.children
import androidx.lifecycle.ViewModelProvider
import com.aelzohry.topsaleqatar.R
import com.aelzohry.topsaleqatar.databinding.FragmentDialogSortBinding
import com.aelzohry.topsaleqatar.utils.base.BaseBottomSheet
import android.widget.RadioButton




class SortDialogFragment : BaseBottomSheet<FragmentDialogSortBinding, SortDialogViewModel>() {

    private lateinit var mListener: Listener
    private var selectedItemId:Int = 0

//    private lateinit var listData: ArrayList<CategoryData>


    fun setListener(listener: Listener) {
        mListener = listener
    }

    companion object {
        fun newInstance(selectedItemId:Int): SortDialogFragment {
            val fragment = SortDialogFragment()
            val args = Bundle()
            args.putInt("selectedItemId",selectedItemId)
            fragment.arguments = args
            return fragment
        }
    }


    override fun getLayoutID(): Int = R.layout.fragment_dialog_sort

    override fun getViewModel(): SortDialogViewModel =
        ViewModelProvider(this)[SortDialogViewModel::class.java]


    override fun setupUI() {

        if (arguments != null) {
            selectedItemId = requireArguments().getInt("selectedItemId")
        }

        if (selectedItemId==0){
            binding.rgSort.check(R.id.rb_latest)
        }else{
//            val count: Int = binding.rgSort.getChildCount()
//            for (i in 0 until count) {
//                val o: View = binding.rgSort.getChildAt(i)
//                if (o is RadioButton) {
//                    if (selectedItemId == o.id){
//                        o.isChecked=true
//                        break
//                    }
//                }
//            }
//            if (binding.rgSort.contain)
            binding.rgSort.check(selectedItemId)
        }

        binding.rgSort.setOnCheckedChangeListener { radioGroup, i ->
            if (mListener != null) {
                mListener.onItemSelected(i)
            }
            Log.e("test_selected", "result " + (i == R.id.rb_latest))
        }

        binding.ibClose.setOnClickListener {
            dismiss()
        }
    }


    override fun observerLiveData() {

    }


    override fun onClickedListener() {

    }

    override fun isFullHeight(): Boolean {
        return false
    }

    interface Listener {
        fun onItemSelected(itemSelected: Int)
    }


}