package com.aelzohry.topsaleqatar.utils;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.aelzohry.topsaleqatar.R;

public class CustomFrame extends FrameLayout {

    private View noInternetView, noItemView, errorView, progressView, layoutView;
    private TextView noItemText;

    public CustomFrame(@NonNull Context context) {
        super(context);
        init();

    }

    public CustomFrame(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomFrame(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomFrame(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        noInternetView = inflater.inflate(R.layout.no_network_view, null);
        noItemView = inflater.inflate(R.layout.empty_view, null);
        errorView = inflater.inflate(R.layout.error_view, null);
        progressView = inflater.inflate(R.layout.loading_view, null);
        noItemText = noItemView.findViewById(R.id.tv);
//        loadingIndicatorView = progressView.findViewById(R.id.loading_indicator);
        this.addView(errorView);
        this.addView(progressView);
        this.addView(noItemView);
        this.addView(noInternetView);
    }

    public void setLayout(View view){
        this.layoutView = view;
    }

    public View getLayoutView() {
        return layoutView;
    }

    public void setNoItemText(String text) {
        noItemText.setText(text);
    }

    private void setNoItemText(int textId) {
        noItemText.setText(getContext().getString(textId));
    }

    public void setVisible(FrameState type) {

        layoutView.setVisibility(type == FrameState.LAYOUT ? View.VISIBLE : View.GONE);
        noItemView.setVisibility(type == FrameState.NO_ITEM ? View.VISIBLE : View.GONE);
        noInternetView.setVisibility(type == FrameState.NO_INTERNET ? View.VISIBLE : View.GONE);
        errorView.setVisibility(type == FrameState.ERROR ? View.VISIBLE : View.GONE);
        progressView.setVisibility(type == FrameState.PROGRESS ? View.VISIBLE : View.GONE);
    }

    public void setHiddenAll() {

        View[] views = new View[]{layoutView, noItemView, noInternetView, errorView, progressView};

        for (int x = 0; x < views.length; x++) {
            views[x].setVisibility(View.GONE);
        }

    }

    public enum FrameState {
        LAYOUT, NO_ITEM, NO_INTERNET, ERROR, PROGRESS,DEFAULT
    }
}
