package com.expensemanager.viewholder;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.expensemanager.R;

public class ListItemViewHolder extends RecyclerView.ViewHolder {

    public TextView listTitle, listAmount, itemQuantityUnit;
    public ImageButton removeItem;

    public ListItemViewHolder(@NonNull View itemView) {
        super(itemView);
        listTitle = itemView.findViewById(R.id.listTitle);
        listAmount = itemView.findViewById(R.id.listAmount);
        itemQuantityUnit = itemView.findViewById(R.id.itemQuantityUnit);
        removeItem = itemView.findViewById(R.id.removeItem);
    }
}
