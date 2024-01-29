package com.expensemanager.viewholder;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.expensemanager.R;

import pl.droidsonroids.gif.GifImageView;

public class IncomeViewHolder extends RecyclerView.ViewHolder {

    public TextView imgText, particularName, accountType, particularDate, particularTime, particularAmount;
    public ImageButton optionBtn;

    public IncomeViewHolder(@NonNull View itemView) {
        super(itemView);

        imgText = itemView.findViewById(R.id.imgText);
        particularName = itemView.findViewById(R.id.particularName);
        accountType = itemView.findViewById(R.id.accountType);
        particularDate = itemView.findViewById(R.id.particularDate);
        particularTime = itemView.findViewById(R.id.particularTime);
        particularAmount = itemView.findViewById(R.id.particularAmount);
        optionBtn = itemView.findViewById(R.id.optionBtn);
    }
}
