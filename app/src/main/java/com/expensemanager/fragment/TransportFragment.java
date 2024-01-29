package com.expensemanager.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.expensemanager.R;
import com.expensemanager.adapter.ExpenseAdapter;
import com.expensemanager.databinding.FragmentTransportBinding;
import com.expensemanager.databse.BudgetDatabase;
import com.expensemanager.databse.ExpenseDatabase;
import com.expensemanager.model.Budget;
import com.expensemanager.model.Expense;
import com.expensemanager.utils.ExpenseUpdateListener;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TransportFragment extends Fragment implements ExpenseUpdateListener{

    FragmentTransportBinding binding;

    private Calendar currentDate = Calendar.getInstance();
    private int currentMonth;
    private int currentYear;

    String particularMonth;
    ExpenseAdapter expenseAdapter;
    Executor executor = Executors.newSingleThreadExecutor();
    ExpenseUpdateListener listener = this;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTransportBinding.inflate(getLayoutInflater(), container, false);

        binding.btnPreviousMonth.setOnClickListener(view -> {
            currentDate.add(Calendar.MONTH, -1);
            updateMonthYear();
            ShowData();
        });

        updateMonthYear();
        ShowData();

        binding.btnNextMonth.setOnClickListener(view -> {
            currentDate.add(Calendar.MONTH, 1);
            updateMonthYear();
            ShowData();
        });


        return binding.getRoot();
    }
    private void ShowData() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                ExpenseDatabase database = ExpenseDatabase.getInstance(getActivity());
                List<Expense> expenseList = database.expenseDao().getAllExpenseByParticularMonthAndCategory(particularMonth, "Transport");

                BudgetDatabase budgetDatabase = BudgetDatabase.getInstance(getActivity());
                Budget budget = budgetDatabase.budgetDao().getBudgetByMonthAndCategory(particularMonth, "Transport");

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        expenseAdapter = new ExpenseAdapter(getActivity(), expenseList, listener);
                        binding.transportRecyclerView.setAdapter(expenseAdapter);

                        if (expenseList.size() == 0){
                            binding.noExpense.setVisibility(View.VISIBLE);
                        }else {
                            binding.noExpense.setVisibility(View.GONE);
                        }
                        int totalExpense = 0;
                        for (Expense expense : expenseList) {
                            totalExpense += expense.getExpense_amount();
                        }
                        binding.transportTotalExpense.setText(String.valueOf(totalExpense));

                        int budgetAmount = 0;

                        if (budget != null){
                            budgetAmount = budget.getBudget_amount();
                        }

                        int consumeAmount = (int) totalExpense;
                        int remainingAmount = budgetAmount-consumeAmount;

                        float percent = ((float) consumeAmount / budgetAmount) * 100;
                        int foodPercent = (int) (percent);
                        int percentValue = (int) percent;

                        binding.transportBudgetAmount.setText(String.valueOf(budgetAmount));
                        binding.transportExpenseAmount.setText(String.valueOf(consumeAmount));
                        binding.transportRemainingAmount.setText(String.valueOf(remainingAmount));

                        if (percentValue <= 49){
                            binding.transportExpenseIndicator.setIndicatorColor(getResources().getColor(R.color.random2));
                            binding.transportPercent.setTextColor(getResources().getColor(R.color.random2));
                        }
                        if (percentValue <= 74 && percentValue >= 50){
                            binding.transportExpenseIndicator.setIndicatorColor(getResources().getColor(R.color.logo_color_1));
                            binding.transportPercent.setTextColor(getResources().getColor(R.color.logo_color_1));
                        }
                        if (percentValue > 75){
                            binding.transportExpenseIndicator.setIndicatorColor(getResources().getColor(R.color.theme_color));
                            binding.transportPercent.setTextColor(getResources().getColor(R.color.theme_color));
                        }

                        binding.transportPercent.setText(String.valueOf(foodPercent)+"%");

                        binding.transportExpenseIndicator.setProgressCompat(percentValue, true);

                        final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                        View dialog = LayoutInflater.from(getActivity()).inflate(R.layout.warning_layout, null);
                        alert.setView(dialog);
                        final AlertDialog alertDialog = alert.create();
                        alertDialog.setCancelable(false);

                        TextView warningHeading = dialog.findViewById(R.id.warningHeading);
                        TextView warningMsg = dialog.findViewById(R.id.warningMsg);
                        TextView budget = dialog.findViewById(R.id.budget);
                        TextView expense = dialog.findViewById(R.id.expense);
                        ImageView cancelBtn = dialog.findViewById(R.id.cancelBtn);
                        CheckBox checkBox = dialog.findViewById(R.id.checkBox);

                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                        boolean exceededTransportShowWarning = sharedPreferences.getBoolean("exceededTransportShowWarning", true);
                        boolean almostTransportShowWarning = sharedPreferences.getBoolean("almostTransportShowWarning", true);

                        if (percentValue > 99 && totalExpense > budgetAmount && exceededTransportShowWarning) {
                            warningHeading.setText("Budget Exceeded");
                            warningMsg.setText("Your transport expense exceeded the budget");
                            budget.setText("Budget: " + String.valueOf(budgetAmount));
                            expense.setText("Expense: " + String.valueOf(totalExpense));
                            alertDialog.show();
                            checkBox.setOnCheckedChangeListener((compoundButton, isChecked) -> {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean("exceededTransportShowWarning", !isChecked);
                                editor.apply();
                            });
                        }

                        if (percentValue >= 90 && percentValue < 100 && almostTransportShowWarning) {
                            warningHeading.setText("Budget Reminder");
                            warningMsg.setText("Your transport budget is almost over");
                            budget.setText("Budget: " + String.valueOf(budgetAmount));
                            expense.setText("Expense: " + String.valueOf(totalExpense));
                            alertDialog.show();
                            checkBox.setOnCheckedChangeListener((compoundButton, isChecked) -> {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean("almostTransportShowWarning", !isChecked);
                                editor.apply();
                            });
                        }

                        cancelBtn.setOnClickListener(view -> {
                            alertDialog.dismiss();
                        });

                    }
                });
            }
        });
    }

    private void updateMonthYear() {
        currentMonth = currentDate.get(Calendar.MONTH);
        currentYear = currentDate.get(Calendar.YEAR);
        String monthName = new DateFormatSymbols().getMonths()[currentMonth];
        binding.monthYearTextView.setText(monthName + " " + currentYear);
        particularMonth = binding.monthYearTextView.getText().toString();
    }

    private int[] getCurrentMonthYear() {
        Calendar currentDate = Calendar.getInstance();
        int particularMonth = currentDate.get(Calendar.MONTH); // Adding 1 to get 1-based month
        int particularYear = currentDate.get(Calendar.YEAR);
        return new int[]{particularMonth, particularYear};
    }

    @Override
    public void ExpenseUpdate(Expense expense) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.edit_expense_layout);

        TextView insertExpense = dialog.findViewById(R.id.insertExpense);
        String[] type = new String[]{"Food", "Housing", "Education", "Shopping", "Transport", "Utilities"};
        ArrayAdapter adapter = new ArrayAdapter<>(
                getContext(),
                R.layout.account_type_list,
                type);

        AutoCompleteTextView particularCategory = dialog.findViewById(R.id.expenseCategory);
        particularCategory.setAdapter(adapter);

        TextInputEditText particularName = dialog.findViewById(R.id.expenseName);
        TextInputEditText particularAmount = dialog.findViewById(R.id.amount);

        particularName.setText(expense.getExpense_particular());
        particularAmount.setText(String.valueOf(expense.getExpense_amount()));
        particularCategory.setText(expense.getExpense_category());

        insertExpense.setOnClickListener(view -> {
            Date currentDateAndTime = new Date();
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());

            int[] monthYear = getCurrentMonthYear();
            int particularMonth = monthYear[0];
            int particularYear = monthYear[1];
            String month = new DateFormatSymbols().getMonths()[particularMonth];

            String expenseParticularDate = String.valueOf(month+" "+particularYear);
            String expenseParticularTime = timeFormat.format(currentDateAndTime);
            String expenseName = particularName.getText().toString();
            String expenseAmount = particularAmount.getText().toString();
            String expenseCategory = particularCategory.getText().toString();

            if (expenseName.equals("")){
                particularName.setError("Required");
            }else if (expenseAmount.equals("")){
                particularAmount.setError("Required");
            }else if (expenseCategory.equals("")){
                particularCategory.setError("Required");
            }else {
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        ExpenseDatabase database = ExpenseDatabase.getInstance(getActivity());

                        Expense addExpense = new Expense();
                        addExpense.setExpense_id(expense.getExpense_id());
                        addExpense.setExpense_particular(expenseName);
                        addExpense.setExpense_amount(Integer.parseInt(expenseAmount));
                        addExpense.setExpense_category(expenseCategory);
                        addExpense.setExpense_date(expense.getExpense_date());
                        addExpense.setExpense_time(expenseParticularTime);
                        addExpense.setExpense_month(expense.getExpense_month());
                        addExpense.setShow_date(expense.getShow_date());
                        addExpense.setExpense_timeStamp(System.currentTimeMillis());

                        database.expenseDao().updateExpense(addExpense);
                    }
                });
                ShowData();
            }
            dialog.dismiss();
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.ExpenseDialogAnimation;
        dialog.getWindow().setGravity(Gravity.TOP);

    }

    @Override
    public void ExpenseDelete(Expense expense) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                ExpenseDatabase database = ExpenseDatabase.getInstance(getActivity());
                database.expenseDao().deleteExpense(expense);
            }
        });
        ShowData();
    }
}