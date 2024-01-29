package com.expensemanager.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.expensemanager.R;
import com.google.android.material.progressindicator.LinearProgressIndicator;

public class BudgetViewHolder extends RecyclerView.ViewHolder {

    public TextView budgetCategory, budgetPercentage, budgetAmount, consumeAmount, remainingAmount, setBudget;
    public LinearProgressIndicator budgetIndicator;

    public BudgetViewHolder(@NonNull View itemView) {
        super(itemView);

        budgetCategory = itemView.findViewById(R.id.budgetCategory);
        budgetPercentage = itemView.findViewById(R.id.budgetPercentage);
        budgetAmount = itemView.findViewById(R.id.budgetAmount);
        consumeAmount = itemView.findViewById(R.id.consumeAmount);
        remainingAmount = itemView.findViewById(R.id.remainingAmount);
        setBudget = itemView.findViewById(R.id.setBudget);
        budgetIndicator = itemView.findViewById(R.id.budgetIndicator);

    }
}
