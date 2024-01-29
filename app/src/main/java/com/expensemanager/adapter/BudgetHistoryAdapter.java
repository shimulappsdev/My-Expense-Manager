package com.expensemanager.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.expensemanager.R;
import com.expensemanager.databse.ExpenseDatabase;
import com.expensemanager.databse.ListDatabase;
import com.expensemanager.model.Budget;
import com.expensemanager.model.Expense;
import com.expensemanager.model.ListItem;
import com.expensemanager.utils.BudgetHistoryListener;
import com.expensemanager.viewholder.BudgetHistoryViewHolder;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class BudgetHistoryAdapter extends RecyclerView.Adapter<BudgetHistoryViewHolder> {

    private Context context;
    private List<Budget> budgetList;
    private BudgetHistoryListener listener;

    private Calendar currentDate = Calendar.getInstance();
    private int currentMonth;
    private int currentYear;
    String particularBGMonth;

    public BudgetHistoryAdapter(Context context, List<Budget> budgetList, BudgetHistoryListener listener) {
        this.context = context;
        this.budgetList = budgetList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BudgetHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BudgetHistoryViewHolder(LayoutInflater.from(context).inflate(R.layout.budget_history_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BudgetHistoryViewHolder holder, int position) {

        Budget budget = budgetList.get(position);

        int budgetAmount = budget.getBudget_amount();

        holder.budgetName.setText(budget.getBudget_name());
        holder.budgetHistoryAmount.setText(String.valueOf(budgetAmount));
        holder.budgetHistoryDate.setText(budget.getBudget_date());
        holder.budgetHistoryTime.setText(budget.getBudget_time());

        updateMonthYear();

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                ExpenseDatabase expenseDatabase = ExpenseDatabase.getInstance(context);
                List<Expense> foodExpense = expenseDatabase.expenseDao().getAllExpenseByParticularMonthAndCategory(particularBGMonth, "Food");
                List<Expense> housingExpense = expenseDatabase.expenseDao().getAllExpenseByParticularMonthAndCategory(particularBGMonth, "Housing");
                List<Expense> educationExpense = expenseDatabase.expenseDao().getAllExpenseByParticularMonthAndCategory(particularBGMonth, "Education");
                List<Expense> shoppingExpense = expenseDatabase.expenseDao().getAllExpenseByParticularMonthAndCategory(particularBGMonth, "Shopping");
                List<Expense> transportExpense = expenseDatabase.expenseDao().getAllExpenseByParticularMonthAndCategory(particularBGMonth, "Transport");
                List<Expense> utilitiesExpense = expenseDatabase.expenseDao().getAllExpenseByParticularMonthAndCategory(particularBGMonth, "Utilities");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int foodConsume = 0;
                        if (foodExpense != null){
                            for (Expense expense: foodExpense){
                                foodConsume += expense.getExpense_amount();
                            }
                        }
                        if (budget.getBudget_name().equals("Food")){
                            holder.consumeHistoryAmount.setText(String.valueOf(foodConsume));
                            holder.remainingHistoryAmount.setText(String.valueOf(budget.getBudget_amount() - foodConsume));
                        }

                        int housingConsume = 0;
                        if (housingExpense != null){
                            for (Expense expense: housingExpense){
                                housingConsume += expense.getExpense_amount();
                            }
                        }
                        if (budget.getBudget_name().equals("Housing")){
                            holder.consumeHistoryAmount.setText(String.valueOf(housingConsume));
                            holder.remainingHistoryAmount.setText(String.valueOf(budget.getBudget_amount() - housingConsume));
                        }

                        int educationConsume = 0;
                        if (educationExpense != null){
                            for (Expense expense: educationExpense){
                                educationConsume += expense.getExpense_amount();
                            }
                        }
                        if (budget.getBudget_name().equals("Education")){
                            holder.consumeHistoryAmount.setText(String.valueOf(educationConsume));
                            holder.remainingHistoryAmount.setText(String.valueOf(budget.getBudget_amount() - educationConsume));
                        }

                        int shoppingConsume = 0;
                        if (shoppingExpense != null){
                            for (Expense expense: shoppingExpense){
                                shoppingConsume += expense.getExpense_amount();
                            }
                        }
                        if (budget.getBudget_name().equals("Shopping")){
                            holder.consumeHistoryAmount.setText(String.valueOf(shoppingConsume));
                            holder.remainingHistoryAmount.setText(String.valueOf(budget.getBudget_amount() - shoppingConsume));
                        }

                        int transportConsume = 0;
                        if (transportExpense != null){
                            for (Expense expense: transportExpense){
                                transportConsume += expense.getExpense_amount();
                            }
                        }
                        if (budget.getBudget_name().equals("Transport")){
                            holder.consumeHistoryAmount.setText(String.valueOf(transportConsume));
                            holder.remainingHistoryAmount.setText(String.valueOf(budget.getBudget_amount() - transportConsume));
                        }

                        int utilitiesConsume = 0;
                        if (utilitiesExpense != null){
                            for (Expense expense: utilitiesExpense){
                                utilitiesConsume += expense.getExpense_amount();
                            }
                        }
                        if (budget.getBudget_name().equals("Utilities")){
                            holder.consumeHistoryAmount.setText(String.valueOf(utilitiesConsume));
                            holder.remainingHistoryAmount.setText(String.valueOf(budget.getBudget_amount() - utilitiesConsume));
                        }

                    }
                });
            }
        });

        holder.clearBtn.setOnClickListener(view -> {
            final AlertDialog.Builder alert = new AlertDialog.Builder(context);
            View mView = LayoutInflater.from(context).inflate(R.layout.delete_history_layout, null);
            alert.setView(mView);
            final AlertDialog alertDialog = alert.create();
            alertDialog.setCancelable(false);
            mView.findViewById(R.id.chancelBTN).setOnClickListener(v -> {
                alertDialog.dismiss();
            });
            mView.findViewById(R.id.okBTN).setOnClickListener(v -> {
                listener.deleteBudgetSingleHistory(budget);
                alertDialog.dismiss();
            });
            alertDialog.show();
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        });

    }

    private void runOnUiThread(Runnable action) {
        if (context instanceof Activity) {
            ((Activity) context).runOnUiThread(action);
        }
    }
    private void updateMonthYear() {
        currentMonth = currentDate.get(Calendar.MONTH);
        currentYear = currentDate.get(Calendar.YEAR);
        String monthName = new DateFormatSymbols().getMonths()[currentMonth];
        particularBGMonth = monthName + " " + currentYear;
    }

    @Override
    public int getItemCount() {
        return budgetList.size();
    }
}
