package com.expensemanager.viewholder;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.expensemanager.R;

public class ItemViewHolder extends RecyclerView.ViewHolder {
    public TextView statusBtn, listTitle, itemQuantityUnit, listAmount;
    public ImageButton itemOptionBtn;
    public ItemViewHolder(@NonNull View itemView) {
        super(itemView);
        statusBtn = itemView.findViewById(R.id.statusBtn);
        listTitle = itemView.findViewById(R.id.listTitle);
        itemQuantityUnit = itemView.findViewById(R.id.itemQuantityUnit);
        listAmount = itemView.findViewById(R.id.listAmount);
        itemOptionBtn = itemView.findViewById(R.id.itemOptionBtn);
    }
}
