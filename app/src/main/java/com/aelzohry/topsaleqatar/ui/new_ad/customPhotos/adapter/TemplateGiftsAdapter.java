package com.aelzohry.topsaleqatar.ui.new_ad.customPhotos.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.aelzohry.topsaleqatar.R;
import com.aelzohry.topsaleqatar.databinding.ItemSmallTemplate1Binding;
import com.aelzohry.topsaleqatar.databinding.ItemSmallTemplate2Binding;
import com.aelzohry.topsaleqatar.databinding.ItemSmallTemplate3Binding;
import com.aelzohry.topsaleqatar.databinding.ItemSmallTemplate4Binding;
import com.aelzohry.topsaleqatar.databinding.ItemSmallTemplate5Binding;
import com.aelzohry.topsaleqatar.databinding.ItemSmallTemplate6Binding;
import com.aelzohry.topsaleqatar.model.CustomImage;

import java.util.ArrayList;

public class TemplateGiftsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final ArrayList<CustomImage> list;
    private final Activity mActivity;
    private final OnItemClickListener listener;
    public static final int TYPE_LAST_ITEM = 0;
    public static final int TYPE_ITEM_1 = 1;
    public static final int TYPE_ITEM_2 = 2;
    public static final int TYPE_ITEM_3 = 3;
    public static final int TYPE_ITEM_4 = 4;
    public static final int TYPE_ITEM_5 = 5;
    public static final int TYPE_ITEM_6 = 6;


    public TemplateGiftsAdapter(Activity mActivity, OnItemClickListener listener) {
        this.list = new ArrayList<>();
        this.mActivity = mActivity;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 0);

        switch (viewType) {
            case TYPE_ITEM_2:
                ItemSmallTemplate2Binding binding2 = ItemSmallTemplate2Binding.inflate(mActivity.getLayoutInflater());
                view = binding2.getRoot();
                view.setLayoutParams(lp);
                return new TemplateTwoViewHolder(view, binding2);
            case TYPE_ITEM_3:
                ItemSmallTemplate3Binding binding3 = ItemSmallTemplate3Binding.inflate(mActivity.getLayoutInflater());
                view = binding3.getRoot();
                view.setLayoutParams(lp);
                return new TemplateThreeViewHolder(view, binding3);
            case TYPE_ITEM_4:
                ItemSmallTemplate4Binding binding4 = ItemSmallTemplate4Binding.inflate(mActivity.getLayoutInflater());
                view = binding4.getRoot();
                view.setLayoutParams(lp);
                return new TemplateFourViewHolder(view, binding4);
            case TYPE_ITEM_5:
                ItemSmallTemplate5Binding binding5 = ItemSmallTemplate5Binding.inflate(mActivity.getLayoutInflater());
                view = binding5.getRoot();
                view.setLayoutParams(lp);
                return new TemplateFiveViewHolder(view, binding5);
            case TYPE_ITEM_6:
                ItemSmallTemplate6Binding binding6 = ItemSmallTemplate6Binding.inflate(mActivity.getLayoutInflater());
                view = binding6.getRoot();
                view.setLayoutParams(lp);
                return new TemplateSixViewHolder(view, binding6);
            default:
                ItemSmallTemplate1Binding binding1 = ItemSmallTemplate1Binding.inflate(mActivity.getLayoutInflater());
                view = binding1.getRoot();
                view.setLayoutParams(lp);
                return new TemplateOneViewHolder(view, binding1);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case TYPE_ITEM_1:
                TemplateOneViewHolder myViewHolder = (TemplateOneViewHolder) holder;
                myViewHolder.bind(list.get(position), position);
                break;
            case TYPE_ITEM_2:
                TemplateTwoViewHolder templateTwoViewHolder = (TemplateTwoViewHolder) holder;
                templateTwoViewHolder.bind(list.get(position), position);
                break;
            case TYPE_ITEM_3:
                TemplateThreeViewHolder templateThreeViewHolder = (TemplateThreeViewHolder) holder;
                templateThreeViewHolder.bind(list.get(position), position);
                break;
            case TYPE_ITEM_4:
                TemplateFourViewHolder templateFourViewHolder = (TemplateFourViewHolder) holder;
                templateFourViewHolder.bind(list.get(position), position);
                break;
            case TYPE_ITEM_5:
                TemplateFiveViewHolder templateFiveViewHolder = (TemplateFiveViewHolder) holder;
                templateFiveViewHolder.bind(list.get(position), position);
                break;
            case TYPE_ITEM_6:
                TemplateSixViewHolder templateSixViewHolder = (TemplateSixViewHolder) holder;
                templateSixViewHolder.bind(list.get(position), position);
                break;
            default:
                TemplateOneViewHolder viewHolder = (TemplateOneViewHolder) holder;
                viewHolder.bind(list.get(position), position);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


    public class TemplateOneViewHolder extends RecyclerView.ViewHolder {
        ItemSmallTemplate1Binding binding;

        TemplateOneViewHolder(View itemView, ItemSmallTemplate1Binding binding) {
            super(itemView);
            this.binding = binding;

        }

        public void bind(CustomImage item, int position) {

            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(position, item);
                }
            });
            if (item.getChecked()) {
                binding.imgSelected.setVisibility(View.VISIBLE);
            } else {
                binding.imgSelected.setVisibility(View.GONE);
            }
        }
    }


    public class TemplateTwoViewHolder extends RecyclerView.ViewHolder {
        ItemSmallTemplate2Binding binding;

        TemplateTwoViewHolder(View itemView, ItemSmallTemplate2Binding binding) {
            super(itemView);
            this.binding = binding;

        }

        public void bind(CustomImage item, int position) {

            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(position, item);
                }
            });

            if (item.getChecked()) {
                binding.imgSelected.setVisibility(View.VISIBLE);
            } else {
                binding.imgSelected.setVisibility(View.GONE);
            }
        }
    }

    public class TemplateThreeViewHolder extends RecyclerView.ViewHolder {
        ItemSmallTemplate3Binding binding;

        TemplateThreeViewHolder(View itemView, ItemSmallTemplate3Binding binding) {
            super(itemView);
            this.binding = binding;

        }

        public void bind(CustomImage item, int position) {


            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(position, item);
                }
            });

            if (item.getChecked()) {
                binding.imgSelected.setVisibility(View.VISIBLE);
            } else {
                binding.imgSelected.setVisibility(View.GONE);
            }
        }
    }

    public class TemplateFourViewHolder extends RecyclerView.ViewHolder {
        ItemSmallTemplate4Binding binding;

        TemplateFourViewHolder(View itemView, ItemSmallTemplate4Binding binding) {
            super(itemView);
            this.binding = binding;

        }

        public void bind(CustomImage item, int position) {

            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(position, item);
                }
            });

            if (item.getChecked()) {
                binding.imgSelected.setVisibility(View.VISIBLE);
            } else {
                binding.imgSelected.setVisibility(View.GONE);
            }
        }
    }

    public class TemplateFiveViewHolder extends RecyclerView.ViewHolder {
        ItemSmallTemplate5Binding binding;

        TemplateFiveViewHolder(View itemView, ItemSmallTemplate5Binding binding) {
            super(itemView);
            this.binding = binding;

        }

        public void bind(CustomImage item, int position) {

            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(position, item);
                }
            });

            if (item.getChecked()) {
                binding.imgSelected.setVisibility(View.VISIBLE);
            } else {
                binding.imgSelected.setVisibility(View.GONE);
            }
        }
    }

    public class TemplateSixViewHolder extends RecyclerView.ViewHolder {
        ItemSmallTemplate6Binding binding;

        TemplateSixViewHolder(View itemView, ItemSmallTemplate6Binding binding) {
            super(itemView);
            this.binding = binding;

        }

        public void bind(CustomImage item, int position) {

            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(position, item);
                }
            });

            if (item.getChecked()) {
                binding.imgSelected.setVisibility(View.VISIBLE);
            } else {
                binding.imgSelected.setVisibility(View.GONE);
            }
        }
    }



    @Override
    public int getItemViewType(int position) {
        return (list.get(position) == null) ? TYPE_LAST_ITEM : list.get(position).getId();
    }


    public void updateSelectedItem(Integer position) {
        for (int i = 0; i < list.size(); i++) {
            CustomImage gift = list.get(i);
            if (i == position) {
                gift.setChecked(true);
            } else {
                gift.setChecked(false);
            }
        }
        notifyDataSetChanged();

    }

    public void remove(int location) {
        if (location >= list.size())
            return;

        list.remove(location);
        notifyItemRemoved(location);
    }

    public void remove() {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == null)
                list.remove(list.get(i));
            notifyDataSetChanged();
        }
    }

    public void removeAll() {
        this.list.clear();
        notifyDataSetChanged();
    }

    public void setData(ArrayList<CustomImage> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void addItem(ArrayList<CustomImage> list) {
        for (CustomImage mc : list) {
//            if (mc != null && !Utils.containsUserId(this.list, mc.getId()))
            add(mc);
        }
        notifyDataSetChanged();
    }

    public void add() {
        list.add(null);
        notifyDataSetChanged();
    }

    public void addLoadingFooter() {
        add();
    }

    public void add(CustomImage mc) {
        list.add(mc);
        notifyItemInserted(list.size() - 1);
    }

    public interface OnItemClickListener {
        void onItemClick(Integer position, CustomImage object);
    }
}
