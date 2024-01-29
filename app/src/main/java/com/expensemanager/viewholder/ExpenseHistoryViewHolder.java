package com.expensemanager.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.expensemanager.R;

public class ExpenseHistoryViewHolder extends RecyclerView.ViewHolder {

    public TextView imgText, expenseParticularName, expenseCategory, expenseDate, expenseTime, expenseParticularAmount;
    public ImageView clearBtn;

    public ExpenseHistoryViewHolder(@NonNull View itemView) {
        super(itemView);

        imgText = itemView.findViewById(R.id.imgText);
        expenseParticularName = itemView.findViewById(R.id.expenseParticularName);
        expenseCategory = itemView.findViewById(R.id.expenseCategory);
        expenseDate = itemView.findViewById(R.id.expenseDate);
        expenseTime = itemView.findViewById(R.id.expenseTime);
        expenseParticularAmount = itemView.findViewById(R.id.expenseParticularAmount);
        clearBtn = itemView.findViewById(R.id.clearBtn);

    }
}
