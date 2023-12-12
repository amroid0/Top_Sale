package com.aelzohry.topsaleqatar.utils.ItemsDialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.aelzohry.topsaleqatar.R;
import com.aelzohry.topsaleqatar.databinding.DialogItemsBinding;
import com.aelzohry.topsaleqatar.databinding.FragmentDialogSortBinding;
import com.aelzohry.topsaleqatar.utils.base.BaseBottomSheet;
import com.aelzohry.topsaleqatar.utils.base.BaseViewModel;

import java.util.ArrayList;
import java.util.List;

public class ItemsDialogFragment extends BaseBottomSheet<DialogItemsBinding, BaseViewModel> implements DialogItemsAdapter.OnClickListener {

    private int type;
    private List<DialogItem> dialogItems;
    private OnItemClickListener listener;
    private DialogItemsAdapter mAdapter;

    public static ItemsDialogFragment newInstance(int type, ArrayList<DialogItem> dialogItems) {
        ItemsDialogFragment officeTypeDialog = new ItemsDialogFragment();
        Bundle args = new Bundle();
        args.putInt("type", type);
        args.putSerializable("dialogItems", dialogItems);
        officeTypeDialog.setArguments(args);
        return officeTypeDialog;

    }

    public static ItemsDialogFragment newInstance(ArrayList<DialogItem> dialogItems) {
        ItemsDialogFragment officeTypeDialog = new ItemsDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable("dialogItems", dialogItems);
        officeTypeDialog.setArguments(args);
        return officeTypeDialog;

    }

    private void getArgumentsData() {
        if (getArguments() != null) {
            this.type = getArguments().getInt("type");
            this.dialogItems = (ArrayList<DialogItem>) getArguments().getSerializable("dialogItems");
        }
    }


    private void initView() {
        mAdapter = new DialogItemsAdapter(getActivity(), dialogItems, this);
        binding.rvItems.setAdapter(mAdapter);
        binding.ibClose.setOnClickListener(v -> dismiss());
    }

    @Override
    public void onItemClick(DialogItem dialogItem) {
        listener.onItemClicked(dialogItem);
        dismiss();
    }


    @Override
    protected int getLayoutID() {
        return R.layout.dialog_items;
    }

    @NonNull
    @Override
    protected BaseViewModel getViewModel() {
        return new ViewModelProvider(this).get(BaseViewModel.class);
    }

    @Override
    protected void setupUI() {
        getArgumentsData();
        initView();
    }

    @Override
    protected void onClickedListener() {

    }

    @Override
    protected void observerLiveData() {

    }

    @Override
    protected boolean isFullHeight() {
        return false;
    }


    public interface OnItemClickListener {
        void onItemClicked(DialogItem dialogItem);
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    @Override
    public void onResume() {
        // Assign window properties to fill the parent

//        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);

//        params.width = WindowManager.LayoutParams.MATCH_PARENT;
//        params.width = width;
//        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        getDialog().getWindow().setAttributes((WindowManager.LayoutParams) params);
//        setCancelable(true);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setOnKeyListener((dialog, keyCode, event) -> {
            if ((keyCode == android.view.KeyEvent.KEYCODE_BACK)) {
                dismiss();
                return true;
            } else return false;
        });
        super.onResume();

    }
}
