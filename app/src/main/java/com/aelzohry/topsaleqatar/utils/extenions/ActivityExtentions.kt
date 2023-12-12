package com.aelzohry.topsaleqatar.utils.extenions

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cc.cloudist.acplibrary.ACProgressConstant
import cc.cloudist.acplibrary.ACProgressFlower
import com.aelzohry.topsaleqatar.R
import com.google.android.material.snackbar.Snackbar
import com.aelzohry.topsaleqatar.model.LocalStanderModel
import com.aelzohry.topsaleqatar.model.StanderModel
import com.aelzohry.topsaleqatar.model.StanderModel1
import com.aelzohry.topsaleqatar.utils.DialogListAdapter
import com.kaopiz.kprogresshud.KProgressHUD
import java.lang.Exception


// show toast msg with StringRes


private val TAG = "activity_extenions"

// Error msg //

//fun Context.showToast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
//
//fun Context.showToast(@StringRes msgRes: Int) = showToast(getString(msgRes))


fun Context.snackBar(msg: String) {
    try {

        Snackbar.make(
            (this as AppCompatActivity).findViewById(android.R.id.content),
            msg, Snackbar.LENGTH_LONG
        ).show()

    } catch (ex: Exception) {

        showToast(msg)

    }
}

fun Context.snackBar(@StringRes msgRes: Int) {
    snackBar(getString(msgRes))
}

fun Context.snackBar(msg: String, actionName: String, onActionClicked: () -> Unit) {

    try {

        Snackbar.make(
            (this as AppCompatActivity).findViewById(android.R.id.content),
            msg,
            Snackbar.LENGTH_LONG
        ).setAction(actionName) {
            onActionClicked()
        }.show()

    } catch (ex: Exception) {

        showToast(msg)
        ex.message?.let { Log.e(TAG, it) }

    }

}

fun Context.snackBar(msgRes: Int, actionName: Int, onActionClicked: () -> Unit) {
    snackBar(getString(msgRes), getString(actionName), onActionClicked)
}

fun Activity.showLoading(): KProgressHUD = KProgressHUD.create(this)
    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
    .setLabel(getString(R.string.loading))
    .setCancellable(false)
    .setAnimationSpeed(2)
    .setDimAmount(0.5f)
//data


//fun String.requestBody(): RequestBody =
//    RequestBody.create(MediaType.parse("text/plain"), this)
//
//fun Int.requestBody(): RequestBody =
//    RequestBody.create(MediaType.parse("text/plain"), this.toString())

//fun AppCompatActivity.hideKeybord() {
//
//    var inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//    inputManager.hideSoftInputFromWindow(
//        currentFocus?.windowToken,
//        InputMethodManager.HIDE_NOT_ALWAYS
//    )
//}

fun AppCompatActivity.setupDialog(
    list: List<String>,
    title: String,
    itemSelected: (position: Int) -> Unit
): Dialog {

    val dialog = Dialog(this)
    dialog.setContentView(R.layout.list_dialog)
    dialog.findViewById<TextView>(R.id.tvTitle)?.text = title
    var listView = dialog.findViewById<ListView>(R.id.listView)
    var adapter = ArrayAdapter<String>(this, R.layout.text_view, list)
    listView?.adapter = adapter
    listView?.setOnItemClickListener { _, _, position, _ ->
        itemSelected(position)
        dialog.dismiss()
    }
    return dialog
}

fun AppCompatActivity.setupDialog(
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


fun AppCompatActivity.setupDialog1(
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


fun AppCompatActivity.setupListDialogs1(dialog: Dialog?, list: List<StanderModel1>, itemSelected: (modelLocal: ArrayList<StanderModel>) -> Unit) {
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


//fun AppCompatActivity.setupDialog(
//    dialog: Dialog?,
//    list: List<LocalStanderModel>,
//    itemSelected: (modelLocal: LocalStanderModel) -> Unit
//) {
//
//    dialog ?: Dialog(this)
//    dialog?.setContentView(R.layout.list_items_dialog)
//    dialog?.window?.setLayout(
//        ViewGroup.LayoutParams.MATCH_PARENT,
//        ViewGroup.LayoutParams.WRAP_CONTENT
//    )
//    val rv = dialog?.findViewById<RecyclerView>(R.id.rv)
//    val et = dialog?.findViewById<EditText>(R.id.etSearch)
//
//    val adapter = DialogListAdapter(list) { it, i ->
//        itemSelected(it)
//        dialog?.dismiss()
//        et?.setText("")
//    }
//    rv?.adapter = adapter
//    rv?.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
//
////    et?.addTextChangedListener(object : TextWatcher {
////        override fun afterTextChanged(s: Editable?) {
////            adapter.filter.filter(s)
////        }
////
////        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
////        }
////
////        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
////        }
////
////    })
//}

fun AppCompatActivity.openActivity(intent: Intent) {

    startActivity(intent)
    try {
        overridePendingTransition(R.anim.anim_in, R.anim.anim_out)


    } catch (ex: Exception) {
        Log.e("error", "Error with cast Context to AppCompatActivity ")
    }
}

fun Resources.dpToPx(dp: Int): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp.toFloat(),
        displayMetrics
    ).toInt()
}


    fun AppCompatActivity.setupDialog(
        dialog: Dialog?,
        list: List<LocalStanderModel>,
        itemSelectedIndex: (modelLocal: LocalStanderModel, i: Int) -> Unit = { _, _ -> },
        itemSelected: (modelLocal: LocalStanderModel) -> Unit = { _ -> },

        ) {

        dialog ?: Dialog(this)
        dialog?.setContentView(R.layout.list_items_dialog)
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
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
        rv?.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
    }

    fun AppCompatActivity.setupListDialogs(
        dialog: Dialog?,
        list: List<StanderModel>,
        itemSelected: (modelLocal: ArrayList<StanderModel>) -> Unit
    ) {
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

    fun AppCompatActivity.setupListDialog(
        dialog: Dialog?,
        list: List<LocalStanderModel>,
        itemSelected: (modelLocal: ArrayList<LocalStanderModel>) -> Unit
    ) {

        dialog ?: Dialog(this)
        dialog?.setContentView(R.layout.list_items_dialog)
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
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
        rv?.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

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





