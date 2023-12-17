package com.aelzohry.topsaleqatar.ui.autoCompleteSearchLocation.view

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.aelzohry.topsaleqatar.R
import com.aelzohry.topsaleqatar.databinding.FragmentSelectMultipleLocationBinding
import com.aelzohry.topsaleqatar.databinding.ItemLocationBinding
import com.aelzohry.topsaleqatar.databinding.ItemLocationResultBinding
import com.aelzohry.topsaleqatar.repository.googleApi.model.PlaceAutocomplete
import com.aelzohry.topsaleqatar.ui.autoCompleteSearchLocation.viewModel.SelectMultipleLocationViewModel
import com.aelzohry.topsaleqatar.utils.base.BaseAdapter
import com.aelzohry.topsaleqatar.utils.base.BaseFragment
import com.aelzohry.topsaleqatar.utils.extenions.setVisible
import com.google.android.material.button.MaterialButton
import org.apmem.tools.layouts.FlowLayout

class SelectMultipleLocationFragment : BaseFragment<FragmentSelectMultipleLocationBinding, SelectMultipleLocationViewModel>() {

    private lateinit var mListener: Listener

    companion object {
        @JvmStatic
        fun newInstance() = SelectMultipleLocationFragment().apply {
            arguments = Bundle().apply {}
        }
    }

    fun setListener(listener: Listener) {
        mListener = listener
    }

    override fun getLayoutID(): Int = R.layout.fragment_select_multiple_location

    override fun getViewModel(): SelectMultipleLocationViewModel = ViewModelProvider(this).get(SelectMultipleLocationViewModel::class.java)

    override fun setupUI() {
        binding.flowLocations.removeAllViews()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Request focus on EditText and show keyboard
        binding.edSearch.requestFocus()
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        val parentFragmentView = requireParentFragment().view

        val parentButton = parentFragmentView?.findViewById<MaterialButton>(R.id.btn_apply)
        parentButton?.visibility =View.VISIBLE
        parentButton?.setOnClickListener {
            mListener.onItemSelected(getItems())
        }

    }

    override fun onPause() {
        super.onPause()

        // Hide the keyboard when the dialog is dismissed
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.hideSoftInputFromWindow(binding.edSearch.windowToken, 0)
    }
    override fun onClickedListener() {
//        binding.ibClose.setOnClickListener { dismiss() }
        binding.edSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                vm.myLoading.postValue(true)
                if (charSequence.length == 0) {
                    setupAdapter(ArrayList())
                } else if (charSequence.length > 0) {
                    binding.rvResult.setVisibility(View.VISIBLE)
                    vm.performSearch(charSequence)
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })

        binding.tvDone.setOnClickListener {
            mListener.onItemSelected(getItems())
        }



        binding.ibView.setOnClickListener { mListener.onMapViewClick() }

    }


    override fun observerLiveData() {
        vm.autoCompleteResult.observe(this) { setupAdapter(it) }
//        vm.isLoading.observe(this) {
//            binding.loading.setVisible(it)
//        }

        vm.myLoading.observe(viewLifecycleOwner) {
            binding.loading.setVisible(it)
        }

    }

    private fun setupAdapter(placeAutocompletes: MutableList<PlaceAutocomplete>) {
        vm.myLoading.postValue(false)

        binding.rvResult.adapter = BaseAdapter<PlaceAutocomplete, ItemLocationResultBinding>(R.layout.item_location_result, vm, placeAutocompletes) { bind, model, position, adapter ->
            bind.llMain.setOnClickListener {
                addNewLocation(model)
            }
        }
    }

    private fun addNewLocation(item: PlaceAutocomplete) {
        if (!isItemAdded(item)) {
            binding.flowLocations.addView(getViewForLocationField(item, binding.flowLocations))
            updateLocationViewsUi()
        }
    }

    private fun isItemAdded(itemToCheck: PlaceAutocomplete): Boolean {
        for (i in 0 until binding.flowLocations.childCount) {
            val v: View = binding.flowLocations.getChildAt(i)
            val tvText = v.findViewById<TextView>(R.id.tv_text)
            val item = tvText.tag as PlaceAutocomplete

            if (TextUtils.isEmpty(itemToCheck.placeId)){
                return false
            }

            if (itemToCheck.placeId.equals(item.placeId)) {
                return true
            }
        }
        return false
    }

    private fun getItems(): ArrayList<PlaceAutocomplete> {
        val list = ArrayList<PlaceAutocomplete>()
        for (i in 0 until binding.flowLocations.childCount) {
            val v: View = binding.flowLocations.getChildAt(i)
            val tvText = v.findViewById<TextView>(R.id.tv_text)
            val item = tvText.tag as PlaceAutocomplete
            list.add(item)

        }
        return list
    }

    private fun getViewForLocationField(item: PlaceAutocomplete, flowFilterFields: FlowLayout): View {
        val vi = requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding: ItemLocationBinding = ItemLocationBinding.inflate(vi)
        val view: View = binding.getRoot()
        if (item.nameDetails != null) {
            binding.tvText.text = item.nameDetails.mainText
        } else {
            binding.tvText.text = item.description
        }
        binding.tvText.setTag(item)


        binding.fBackground.setOnClickListener { v ->
            flowFilterFields.removeView(view)
            updateLocationViewsUi()
        }
        return view
    }

    private fun updateLocationViewsUi() {
        binding.flowLocations.setVisible(binding.flowLocations.childCount > 0)
    }

    interface Listener {
        fun onItemSelected(list: ArrayList<PlaceAutocomplete>)
        fun onMapViewClick()
    }

}