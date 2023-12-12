package com.aelzohry.topsaleqatar.ui.ad_details.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.aelzohry.topsaleqatar.R;
import com.aelzohry.topsaleqatar.model.AdsItem;

import java.util.ArrayList;


public class AdsDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<AdsItem> aqarItems;
    private Activity mActivity;

    public AdsDetailsAdapter(ArrayList<AdsItem> aqarItems, Activity mActivity) {
        this.aqarItems = aqarItems;
        this.mActivity = mActivity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View view = LayoutInflater.from(mActivity).inflate(R.layout.item_ads_details, parent, false);
        viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyViewHolder myViewHolder = (MyViewHolder) holder;
        setData(myViewHolder, getItem(position));
    }

    private void setData(MyViewHolder myViewHolder, AdsItem item) {

        if (myViewHolder.getAbsoluteAdapterPosition() == 0) {
            myViewHolder.rootView.setBackgroundResource(R.color.bg_color_ads_details);
        } else if (myViewHolder.getAbsoluteAdapterPosition() % 2 == 0) {
            myViewHolder.rootView.setBackgroundResource(R.color.bg_color_ads_details);
        } else {
            myViewHolder.rootView.setBackgroundResource(R.color.bg_color_ads_details_1);
        }

        myViewHolder.tvText.setText(item.getTitle());
//        myViewHolder.tvValue.setText(ToolUtils.getSpannedText(item.getValue(), mActivity));
        myViewHolder.tvValue.setText(item.getValue());
    }

    public AdsItem getItem(int pos) {
        return aqarItems.get(pos);
    }

    @Override
    public int getItemCount() {
        return aqarItems.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        /**
         * ButterKnife Code
         **/
        RelativeLayout rootView;
        TextView tvText;
        TextView tvValue;

        /**
         * ButterKnife Code
         **/
        public MyViewHolder(View itemView) {
            super(itemView);
            rootView= itemView.findViewById(R.id.root_view);
            tvText= itemView.findViewById(R.id.tv_text);
            tvValue= itemView.findViewById(R.id.tv_value);

        }
    }
}
