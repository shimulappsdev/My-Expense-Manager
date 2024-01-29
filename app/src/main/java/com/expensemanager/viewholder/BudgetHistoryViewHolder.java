package com.expensemanager.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.expensemanager.R;

public class BudgetHistoryViewHolder extends RecyclerView.ViewHolder {

    public TextView budgetName, budgetHistoryAmount, consumeHistoryAmount, remainingHistoryAmount, budgetHistoryDate, budgetHistoryTime;
    public ImageView clearBtn;

    public BudgetHistoryViewHolder(@NonNull View itemView) {
        super(itemView);

        budgetName = itemView.findViewById(R.id.budgetName);
        budgetHistoryAmount = itemView.findViewById(R.id.budgetHistoryAmount);
        consumeHistoryAmount = itemView.findViewById(R.id.consumeHistoryAmount);
        remainingHistoryAmount = itemView.findViewById(R.id.remainingHistoryAmount);
        budgetHistoryDate = itemView.findViewById(R.id.budgetHistoryDate);
        budgetHistoryTime = itemView.findViewById(R.id.budgetHistoryTime);
        clearBtn = itemView.findViewById(R.id.clearBtn);

    }
}
