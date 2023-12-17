package com.aelzohry.topsaleqatar.ui.home;

import static com.aelzohry.topsaleqatar.utils.Binding.setImage;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Point;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aelzohry.topsaleqatar.R;
import com.aelzohry.topsaleqatar.databinding.ItemSliderBinding;
import com.aelzohry.topsaleqatar.model.TopBanner;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SliderDataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final ArrayList<TopBanner> adstList;
    private final Activity mActivity;
    private final OnItemClickListener listener;
    private int width_px;
    private int height_px;

    public SliderDataAdapter(Activity mActivity, ArrayList<TopBanner> data, OnItemClickListener listener) {
        this.adstList = data;
        this.mActivity = mActivity;
        this.listener = listener;
    }

    @Override
    public void onAttachedToRecyclerView(@NotNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        Activity context = (Activity) recyclerView.getContext();
        Point windowDimensions = new Point();
        context.getWindowManager().getDefaultDisplay().getSize(windowDimensions);
//        height_px = 200;
        height_px = context.getResources().getDimensionPixelSize(R.dimen.slider_h);
        width_px = Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSliderBinding binding = ItemSliderBinding.inflate(mActivity.getLayoutInflater());
        View view = binding.getRoot();
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                Math.round(width_px /** 0.8f*/),
                Math.round(height_px /** 0.8f*/));
        view.setLayoutParams(params);
        return new MyViewHolder(view, binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder RestaurantViewHolder = (MyViewHolder) holder;

        RestaurantViewHolder.bind(position);
    }

    @Override
    public int getItemCount() {
        return adstList == null ? 0 : adstList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ItemSliderBinding binding;

        public MyViewHolder(View itemView, ItemSliderBinding binding) {
            super(itemView);
            this.binding = binding;
            binding.rootLayout.setOnClickListener(v -> {
                if (listener != null)
                    listener.onItemClick(getAdapterPosition());
            });
            binding.rlContent.setOnClickListener(v -> {
                if (listener != null)
                    listener.onItemClick(getAdapterPosition());
            });
        }

        public void bind(int position) {
            TopBanner adsBean = adstList.get(position);
            setImage(mActivity, binding.imageView, adsBean.getPhotoUrl());
        }
    }

    public void addLoadingFooter() {
        add(null);
    }

    public void remove(int location) {
        if (location >= adstList.size())
            return;
        adstList.remove(location);
        notifyItemRemoved(location);
    }

    public void remove() {
        for (int i = 0; i < adstList.size(); i++) {
            if (adstList.get(i) == null)
                adstList.remove(adstList.get(i));
            notifyDataSetChanged();
        }
    }

    public void removeAll() {
        this.adstList.clear();
        notifyDataSetChanged();

    }

    public void setData(ArrayList<TopBanner> adstList) {
        this.adstList.clear();
        this.adstList.addAll(adstList);
        notifyDataSetChanged();
    }

    public void addItem(ArrayList<TopBanner> Restaurants) {
        for (TopBanner mc : Restaurants) {
//            if (mc != null && !Utils.containsAdsId(adstList, mc.getId()))
            add(mc);
        }
        notifyDataSetChanged();
    }

    public void add() {
        adstList.add(null);
        notifyDataSetChanged();
    }

    public void add(TopBanner mc) {
        adstList.add(mc);
        notifyItemInserted(adstList.size() - 1);
    }

    public interface OnItemClickListener {
        void onItemClick(Integer position);
    }
}
