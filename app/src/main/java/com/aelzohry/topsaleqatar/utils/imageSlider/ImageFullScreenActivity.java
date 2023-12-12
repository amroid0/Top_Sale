package com.aelzohry.topsaleqatar.utils.imageSlider;

import static com.gw.swipeback.SwipeBackLayout.FROM_TOP;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.aelzohry.topsaleqatar.R;
import com.aelzohry.topsaleqatar.databinding.ActivityImageFullScreenBinding;
import com.aelzohry.topsaleqatar.utils.imageSlider.adapter.PagerImageAdapter;
import com.gw.swipeback.SwipeBackLayout;

import java.util.ArrayList;

public class ImageFullScreenActivity extends AppCompatActivity {


    private PagerImageAdapter adapter;
    private ArrayList<ImageSlider> arrayMediaData;
    private int position;
    private ActivityImageFullScreenBinding binding;

    public static Intent newInstance(Context context, ArrayList<ImageSlider> arrayList, int position) {
        Intent intent = new Intent(context, ImageFullScreenActivity.class);
        intent.putExtra("arrayMediaData", arrayList);
        intent.putExtra("position", position);
        return intent;
    }

    private void getIntentData() {
        arrayMediaData = (ArrayList<ImageSlider>) getIntent().getSerializableExtra("arrayMediaData");
        position = getIntent().getIntExtra("position", 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        binding = ActivityImageFullScreenBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        initSwipe();
        getIntentData();
        initPager();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void initPager() {
//        if (!Helper.INSTANCE.isEnglish()) {
//            Collections.reverse(arrayMediaData);
//        }
        adapter = new PagerImageAdapter(this, arrayMediaData);
        adapter.notifyDataSetChanged();

        binding.viewpager.setAdapter(adapter);
        binding.viewpager.setCurrentItem(position);
//        binding.viewpager.setOffscreenPageLimit(0);


    }


    private void initSwipe() {
        SwipeBackLayout mSwipeBackLayout = new SwipeBackLayout(this);
        mSwipeBackLayout.attachToActivity(this);
        mSwipeBackLayout.setDirectionMode(FROM_TOP);
        mSwipeBackLayout.setMaskAlpha(0);
        mSwipeBackLayout.setSwipeBackFactor(0.5f);
        mSwipeBackLayout.setBackgroundColor(ContextCompat.getColor(this,android.R.color.transparent));
    }


}
