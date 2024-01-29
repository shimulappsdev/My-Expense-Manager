package com.expensemanager.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.expensemanager.R;
import com.expensemanager.activity.MainActivity;
import com.expensemanager.model.Budget;
import com.expensemanager.viewholder.BudgetViewHolder;

import java.util.List;

public class BudgetAdapter extends RecyclerView.Adapter<BudgetViewHolder> {

    private Context context;
    private List<Budget> budgetList;

    public BudgetAdapter(Context context, List<Budget> budgetList) {
        this.context = context;
        this.budgetList = budgetList;
    }

    @NonNull
    @Override
    public BudgetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BudgetViewHolder(LayoutInflater.from(context).inflate(R.layout.budget_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BudgetViewHolder holder, int position) {

        Budget budget = budgetList.get(position);
        String budgetCategory = budget.getBudget_name();
        holder.budgetCategory.setText(budgetCategory);

        int budgetAmount = budget.getBudget_amount();
        int remainingAmount = budgetAmount - 0;

        float percent = ((float) 0 / budgetAmount) * 100;
        int percentValue = (int) percent;

        holder.budgetIndicator.setProgressCompat(percentValue, true);


        holder.budgetAmount.setText(String.valueOf(budgetAmount));
        holder.remainingAmount.setText(String.valueOf(remainingAmount));
        holder.budgetPercentage.setText(String.valueOf(percentValue)+"%");

        holder.setBudget.setOnClickListener(view -> {

        });

    }

    private void BudgetSetDialog() {



    }

    @Override
    public int getItemCount() {
        return budgetList.size();
    }
}
