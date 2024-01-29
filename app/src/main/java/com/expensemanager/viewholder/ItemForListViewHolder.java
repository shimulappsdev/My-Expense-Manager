package com.expensemanager.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.expensemanager.R;

public class ItemForListViewHolder extends RecyclerView.ViewHolder {

    public TextView itemForListTextView;

    public ItemForListViewHolder(@NonNull View itemView) {
        super(itemView);

        itemForListTextView = itemView.findViewById(R.id.itemForListTextView);

    }
}
