package com.aelzohry.topsaleqatar.ui.changeLanguage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.aelzohry.topsaleqatar.R;
import com.aelzohry.topsaleqatar.databinding.FragmentChangeLanguageDialogBinding;
import com.aelzohry.topsaleqatar.helper.AppLanguage;
import com.aelzohry.topsaleqatar.utils.base.BaseBottomSheet;
import com.aelzohry.topsaleqatar.utils.base.BaseViewModel;


public class ChangeLanguageDialogFragment extends BaseBottomSheet<FragmentChangeLanguageDialogBinding, BaseViewModel> {


    private LanguageListener listener;


    public ChangeLanguageDialogFragment() {
        // Required empty public constructor
    }


    public static ChangeLanguageDialogFragment newInstance() {
        ChangeLanguageDialogFragment fragment = new ChangeLanguageDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public int getLayoutID() {
        return R.layout.fragment_change_language_dialog;
    }


    @NonNull
    @Override
    protected BaseViewModel getViewModel() {
        return new ViewModelProvider(this).get(BaseViewModel.class);
    }

    @Override
    protected void setupUI() {

    }

    @Override
    protected void onClickedListener() {
        binding.tvEnglish.setOnClickListener(v -> {
            if (listener != null) listener.onItemClicked(AppLanguage.English);
            dismiss();
        });
        binding.tvArabic.setOnClickListener(v -> {
            if (listener != null) listener.onItemClicked(AppLanguage.Arabic);
            dismiss();
        });

        binding.ibClose.setOnClickListener(v -> {
            if (listener != null) listener.onItemClicked(null);
            dismiss();
        });

        if (getDialog() != null) {
            getDialog().setOnCancelListener(dialogInterface -> {
                if (listener != null) listener.onItemClicked(null);
            });
            getDialog().setOnDismissListener(dialogInterface -> {
                if (listener != null) listener.onItemClicked(null);
            });
        }

    }


    @Override
    protected void observerLiveData() {

    }

    @Override
    protected boolean isFullHeight() {
        return false;
    }


    public interface LanguageListener {
        void onItemClicked(AppLanguage language);
    }

    public void setListener(LanguageListener listener) {
        this.listener = listener;
    }
}