package com.expensemanager.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.expensemanager.R;
import com.expensemanager.adapter.BudgetHistoryAdapter;
import com.expensemanager.adapter.IncomeHistoryAdapter;
import com.expensemanager.databinding.FragmentBudgetHistoryBinding;
import com.expensemanager.databse.BudgetDatabase;
import com.expensemanager.databse.ExpenseDatabase;
import com.expensemanager.databse.IncomeDatabase;
import com.expensemanager.model.Budget;
import com.expensemanager.model.Income;
import com.expensemanager.utils.BudgetHistoryListener;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class BudgetHistoryFragment extends Fragment implements BudgetHistoryListener {

    FragmentBudgetHistoryBinding binding;
    Executor executor = Executors.newSingleThreadExecutor();
    BudgetHistoryListener listener = this;
    BudgetHistoryAdapter budgetHistoryAdapter;
    String item;
    private Calendar currentDate = Calendar.getInstance();
    private int currentMonth;
    private int currentYear;
    String particularMonth, monthName, previousMonth, previousMonthName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBudgetHistoryBinding.inflate(getLayoutInflater(), container, false);

        updateMonthYear();
        ShowData();

        binding.clearHistoryBtn.setOnClickListener(view -> {
            final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            View dialog = LayoutInflater.from(getActivity()).inflate(R.layout.clear_history_layout, null);
            alert.setView(dialog);

            final AlertDialog alertDialog = alert.create();
            alertDialog.setCancelable(true);

            TextView clearText =  dialog.findViewById(R.id.clearText);
            Spinner selectHistory = dialog.findViewById(R.id.selectHistory);
            AppCompatButton clearHistory = dialog.findViewById(R.id.clearHistory);

            clearText.setText("Clear Budget History");

            selectHistory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                    item = adapterView.getItemAtPosition(position).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });

            ArrayList<String> histories = new ArrayList<>();
            histories.add("-select-");
            histories.add("Previous Month Only");
            histories.add("All Previous Months");

            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.list_category_iten_layout, histories);
            adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
            selectHistory.setAdapter(adapter);

            clearHistory.setOnClickListener(view1 -> {
                final AlertDialog.Builder alert1 = new AlertDialog.Builder(getActivity());
                View mView = LayoutInflater.from(getActivity()).inflate(R.layout.delete_history_layout, null);
                alert1.setView(mView);
                final AlertDialog alertDialog1 = alert1.create();
                alertDialog1.setCancelable(false);
                mView.findViewById(R.id.chancelBTN).setOnClickListener(v -> {
                    alertDialog1.dismiss();
                });
                mView.findViewById(R.id.okBTN).setOnClickListener(v -> {
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            BudgetDatabase budgetDatabase = BudgetDatabase.getInstance(getActivity());
                            if (item.equals("Previous Month Only")){
                                budgetDatabase.budgetDao().deletePreviousMonthBudget(previousMonth);
                            }
                            if (item.equals("All Previous Months")){
                                budgetDatabase.budgetDao().deleteAllPreviousMonthBudget(particularMonth);
                            }
                        }
                    });
                    alertDialog1.dismiss();
                });
                alertDialog1.show();
                alertDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                alertDialog.dismiss();
                ShowData();
            });

            alertDialog.show();
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        });

        binding.backBtn.setOnClickListener(view -> {
            getActivity().finish();
        });


        return binding.getRoot();
    }
    private void ShowData() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                BudgetDatabase budgetDatabase = BudgetDatabase.getInstance(getActivity());
                List<Budget> budgetList = budgetDatabase.budgetDao().getAllBudget();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (budgetList != null){
                            budgetHistoryAdapter = new BudgetHistoryAdapter(getActivity(), budgetList, listener);
                            binding.budgetHistoryRecyclerView.setAdapter(budgetHistoryAdapter);
                        }
                    }
                });
            }
        });
    }

    @Override
    public void deleteBudgetSingleHistory(Budget budget) {
        if (budget.getBudget_date().equals(particularMonth)){
            Toast.makeText(getActivity(), "Can't Delete Current Month's Data", Toast.LENGTH_SHORT).show();
        }else {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    BudgetDatabase budgetDatabase = BudgetDatabase.getInstance(getActivity());
                    budgetDatabase.budgetDao().deleteBudget(budget);
                }
            });
            ShowData();
        }
    }
    private void updateMonthYear() {
        currentMonth = currentDate.get(Calendar.MONTH);
        currentYear = currentDate.get(Calendar.YEAR);
        monthName = new DateFormatSymbols().getMonths()[currentMonth];
        previousMonthName = new DateFormatSymbols().getMonths()[currentMonth-1];
        particularMonth = monthName + " " + currentYear;
        previousMonth = previousMonthName + " " + currentYear;
    }
}