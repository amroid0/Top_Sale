package com.aelzohry.topsaleqatar.utils;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.SupportMapFragment;


public class WorkaroundMapFragment extends SupportMapFragment {
    private boolean scrollable = true;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (view != null) {
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (!scrollable && event.getPointerCount() > 1) {
                        // Prevent scroll of bottom sheet if scrollable is set to false and multi-touch (zooming) occurs
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                    }
                    return false;
                }
            });
        }
        return view;
    }

    public void setScrollable(boolean scrollable) {
        this.scrollable = scrollable;
    }
}