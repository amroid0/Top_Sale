package com.aelzohry.topsaleqatar.utils.extenions

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aelzohry.topsaleqatar.R
import com.aelzohry.topsaleqatar.model.LocalStanderModel
import com.aelzohry.topsaleqatar.model.StanderModel
import com.aelzohry.topsaleqatar.model.StanderModel1
import com.aelzohry.topsaleqatar.utils.DialogListAdapter

fun Fragment.showToast(msg: String) {
    requireContext().showToast(msg)
}

fun Fragment.showToast(@StringRes msgId: Int) {
    requireContext().showToast(msgId)
}

fun Fragment.snackBar(msg: String) {
    requireContext().snackBar(msg)
}

fun Fragment.snackBar(@StringRes msgId: Int) {
    requireContext().snackBar(msgId)
}

fun Fragment.snackBar(msg: String, actionName: String, onActionClicked: () -> Unit = {}) {

    requireContext().snackBar(msg, actionName, onActionClicked)
//    try {
//
//        val snack = Snackbar.make(
//            requireActivity().findViewById(android.R.id.content),
//            msg,
//            Snackbar.LENGTH_LONG
//        )
//
//        actionName?.let {
//            snack.setAction(actionName) {
//                onActionClicked()
//            }
//        }
//        snack.show()
//
//    } catch (ex: Exception) {
//
//        showToast(msg)
//
//    }

}

fun Fragment.snackBar(@StringRes msgId: Int, @StringRes actionNameId: Int, onActionClicked: () -> Unit = {}) {
    requireContext().snackBar(msgId, actionNameId, onActionClicked)
}

fun Fragment.setupDialog(list: List<String>, title: String, itemSelected: (position: Int) -> Unit): Dialog {

    val dialog = Dialog(requireContext())
    dialog.setContentView(R.layout.list_dialog)
    dialog.findViewById<TextView>(R.id.tvTitle)?.text = title
    var listView = dialog.findViewById<ListView>(R.id.listView)
    var adapter = ArrayAdapter<String>(requireContext(), R.layout.text_view, list)
    listView?.adapter = adapter
    listView?.setOnItemClickListener { _, _, position, _ ->
        itemSelected(position)
        dialog.dismiss()
    }
    return dialog
}

fun Fragment.setupDialog(
    dialog: Dialog?,
    list: List<StanderModel>,
    itemSelected: (modelLocal: StanderModel) -> Unit = { _ -> },

    ) {
    val newList = ArrayList<LocalStanderModel>()
    list.forEach {
        newList.add(LocalStanderModel(it._id, LocalStanderModel.LocalizedModel(it.title, it.title)))
    }
    setupDialog(dialog, newList) {
        itemSelected(StanderModel(it._id, it.title.localized))
    }
}

fun Fragment.setupDialog1(
    dialog: Dialog?,
    list: List<StanderModel1>,
    itemSelected: (modelLocal: StanderModel) -> Unit = { _ -> },

    ) {
    val newList = ArrayList<LocalStanderModel>()
    list.forEach {
        newList.add(LocalStanderModel(it._id, LocalStanderModel.LocalizedModel(it.title, it.title)))
    }
    setupDialog(dialog, newList) {
        itemSelected(StanderModel(it._id, it.title.localized))
    }
}

fun Fragment.setupDialog(
    dialog: Dialog?,
    list: List<LocalStanderModel>,
    itemSelectedIndex: (modelLocal: LocalStanderModel, i: Int) -> Unit = { _, _ -> },
    itemSelected: (modelLocal: LocalStanderModel) -> Unit = { _ -> },

    ) {

    dialog ?: Dialog(requireContext())
    dialog?.setContentView(R.layout.list_items_dialog)
    dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    val rv = dialog?.findViewById<RecyclerView>(R.id.rv)
    val et = dialog?.findViewById<EditText>(R.id.etSearch)
    dialog?.findViewById<View>(R.id.btn_close)?.setOnClickListener {
        dialog?.dismiss()
    }

    val adapter = DialogListAdapter(list) { it, i ->
        itemSelected(it)
        itemSelectedIndex(it, i)
        dialog?.dismiss()
        et?.setText("")
    }
    rv?.adapter = adapter
    rv?.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
}

fun Fragment.setupListDialogs(dialog: Dialog?, list: List<StanderModel>, itemSelected: (modelLocal: ArrayList<StanderModel>) -> Unit) {
    val newList = ArrayList<LocalStanderModel>()
    list.forEach {
        newList.add(LocalStanderModel(it._id, LocalStanderModel.LocalizedModel(it.title, it.title)))
    }
    setupListDialog(dialog, newList) {
        val sa = ArrayList<StanderModel>()
        it.forEach {
            sa.add(StanderModel(it._id, it.title.localized))
        }
        itemSelected(sa)
    }
}

fun Fragment.setupListDialogs1(dialog: Dialog?, list: List<StanderModel1>, itemSelected: (modelLocal: ArrayList<StanderModel>) -> Unit) {
    val newList = ArrayList<LocalStanderModel>()
    list.forEach {
        newList.add(LocalStanderModel(it._id, LocalStanderModel.LocalizedModel(it.titleEn, it.titleAr)))
    }
    setupListDialog(dialog, newList) {
        val sa = ArrayList<StanderModel>()
        it.forEach {
            sa.add(StanderModel(it._id, it.title.localized))
        }
        itemSelected(sa)
    }
}

fun Fragment.setupListDialog(dialog: Dialog?, list: List<LocalStanderModel>, itemSelected: (modelLocal: ArrayList<LocalStanderModel>) -> Unit) {

    dialog ?: Dialog(requireContext())
    dialog?.setContentView(R.layout.list_items_dialog)
    dialog?.window?.setLayout((resources.displayMetrics.widthPixels * 0.90).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)

    dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    val rv = dialog?.findViewById<RecyclerView>(R.id.rv)
    val et = dialog?.findViewById<EditText>(R.id.etSearch)
    dialog?.findViewById<View>(R.id.btn_close)?.setOnClickListener {
        dialog?.dismiss()
    }
    val selectedList = ArrayList<LocalStanderModel>()
    val adapter = DialogListAdapter(list, true) { it, i ->
        selectedList.add(it)
//        itemSelected(it)
//        dialog?.dismiss()
//        et?.setText("")
    }
    rv?.adapter = adapter
    rv?.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))

    dialog?.findViewById<View>(R.id.btn_apply)?.setVisible(true)
    dialog?.findViewById<View>(R.id.btn_apply)?.setOnClickListener {
        itemSelected(adapter.getSelectedList())
        dialog?.dismiss()
    }
//    et?.addTextChangedListener(object : TextWatcher {
//        override fun afterTextChanged(s: Editable?) {
////            adapter.filter.filter(s)
//        }
//
//        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//        }
//
//        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//        }
//
//    })
}

fun Fragment.hideKeyboard() {
    val imm = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    var view = activity?.currentFocus
    if (view == null) view = View(activity)
    imm.hideSoftInputFromWindow(view.windowToken, 0)


}/*
public static void hideKeyboard(Activity activity) {
    //Find the currently focused view, so we can grab the correct window token from it.
    View  =;
    //If no view currently has focus, create a new one, just so we can grab a window token from it

    ;
* */
//fun Fragment.openMap(requestCode: Int = Constant.REQUEST_LOCATION) {
//
//    val locationPickerIntent = LocationPickerActivity.Builder()
//        .withLocation(SharedPref().lat.toDouble(), SharedPref().lat.toDouble())
//        .withGeolocApiKey(getString(R.string.google_maps_key))
//        .withSearchZone("ar_AR")
//        .shouldReturnOkOnBackPressed()
//        .withStreetHidden()
//        .withCityHidden()
//        .withZipCodeHidden()
//        .withSatelliteViewHidden()
//        .withGoogleTimeZoneEnabled()
//        .withVoiceSearchHidden()
//        .build(requireContext())
//    startActivityForResult(locationPickerIntent, requestCode)
////    try {
////
////        startActivityForResult(
////            PlacePicker.IntentBuilder().build(requireActivity()),
////            requestCode
////        )
////
////    } catch (e: GooglePlayServicesRepairableException) {
////        e.printStackTrace()
////    } catch (e: GooglePlayServicesNotAvailableException) {
////        e.printStackTrace()
////    }
//}