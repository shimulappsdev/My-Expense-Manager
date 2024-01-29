package com.expensemanager.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.expensemanager.R;

public class ListViewHolder extends RecyclerView.ViewHolder {

    public TextView listHeading, category, listDate;
    public RecyclerView itemForLisLayout;
    public ImageView optionBtn;
    public ConstraintLayout listMainLayout;
    public CardView itemCard;

    public ListViewHolder(@NonNull View itemView) {
        super(itemView);

        listHeading = itemView.findViewById(R.id.listHeading);
        category = itemView.findViewById(R.id.category);
        listDate = itemView.findViewById(R.id.listDate);
        itemForLisLayout = itemView.findViewById(R.id.itemForLisLayout);
        optionBtn = itemView.findViewById(R.id.optionBtn);
        listMainLayout = itemView.findViewById(R.id.listMainLayout);
        itemCard = itemView.findViewById(R.id.itemCard);

    }
}
