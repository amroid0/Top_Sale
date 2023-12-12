package com.aelzohry.topsaleqatar.utils.imageSlider.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;


import com.aelzohry.topsaleqatar.utils.imageSlider.ImagePagerFragment;
import com.aelzohry.topsaleqatar.utils.imageSlider.ImageSlider;

import java.util.ArrayList;

/**
 * # Created by Mousa Hashihso on 02/03/2022.
 */
public class PagerImageAdapter extends FragmentStateAdapter {
    public PagerImageAdapter(FragmentActivity fm) {
        super(fm);
    }

    ArrayList<ImageSlider> arrSlider = new ArrayList<>();

    public PagerImageAdapter(FragmentActivity fm, ArrayList<ImageSlider> arrSlider) {
        super(fm);
        this.arrSlider = arrSlider;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return ImagePagerFragment.newInstance(arrSlider, position);
    }

    @Override
    public int getItemCount() {
        return arrSlider.size();
    }
}
