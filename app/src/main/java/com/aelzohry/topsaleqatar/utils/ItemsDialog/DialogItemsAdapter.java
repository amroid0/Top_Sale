package com.aelzohry.topsaleqatar.utils.ItemsDialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.aelzohry.topsaleqatar.R;

import java.util.List;


public class DialogItemsAdapter extends RecyclerView.Adapter<DialogItemsAdapter.MyViewHolder> {

    private final Context context;
    private List<DialogItem> dialogItems;
    private final OnClickListener listener;


    public DialogItemsAdapter(Context context, List<DialogItem> dialogItems, OnClickListener listener) {
        this.context = context;
        this.dialogItems = dialogItems;
        this.listener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        /**
         * ButterKnife Code
         **/
        TextView tvText;

        /**
         * ButterKnife Code
         **/

        public MyViewHolder(View itemView) {
            super(itemView);
            tvText = itemView.findViewById(R.id.tv_text);


        }

        public void bind(final DialogItem dialogItem) {
            tvText.setText(dialogItem.getText() + "");
            itemView.setOnClickListener(view -> {
                listener.onItemClick(dialogItem);
            });
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dialog, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.bind(dialogItems.get(position));
    }

    @Override
    public int getItemCount() {
        return dialogItems.size();
    }


    public interface OnClickListener {
        void onItemClick(DialogItem dialogItem);
    }

}
