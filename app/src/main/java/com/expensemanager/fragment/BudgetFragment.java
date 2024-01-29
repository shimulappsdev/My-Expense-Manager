package com.expensemanager.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.expensemanager.R;
import com.expensemanager.activity.MainActivity;
import com.expensemanager.adapter.BudgetAdapter;
import com.expensemanager.adapter.IncomeAdapter;
import com.expensemanager.databinding.FragmentBudgetBinding;
import com.expensemanager.databse.BudgetDatabase;
import com.expensemanager.databse.ExpenseDatabase;
import com.expensemanager.databse.IncomeDatabase;
import com.expensemanager.model.Budget;
import com.expensemanager.model.Expense;
import com.expensemanager.model.Income;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class BudgetFragment extends Fragment {

    FragmentBudgetBinding binding;
    private Calendar currentDate = Calendar.getInstance();
    private int currentMonth;
    private int currentYear;
    String particularBGMonth;
    Executor executor = Executors.newSingleThreadExecutor();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBudgetBinding.inflate(getLayoutInflater(), container, false);

        binding.btnPreviousMonth.setOnClickListener(view -> {
            currentDate.add(Calendar.MONTH, -1);
            updateMonthYear();
            ShowData();
        });

        updateMonthYear();

        binding.btnNextMonth.setOnClickListener(view -> {
            currentDate.add(Calendar.MONTH, 1);
            updateMonthYear();
            ShowData();
        });

        binding.backBtn.setOnClickListener(view -> {
            refresh();
        });

        binding.setFoodBudget.setOnClickListener(view -> {
            final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            View dialog = LayoutInflater.from(getActivity()).inflate(R.layout.set_budget_layout, null);
            alert.setView(dialog);

            final AlertDialog alertDialog = alert.create();
            alertDialog.setCancelable(true);

            TextView title =  dialog.findViewById(R.id.setTitle);
            EditText setAmount = dialog.findViewById(R.id.setBudgetAmount);
            AppCompatButton setBtn = dialog.findViewById(R.id.setBudgetBtn);

            title.setText("Set Food Budget");

            setBtn.setOnClickListener(view1 -> {

                Date currentDateAndTime = new Date();
                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());

                int[] monthYear = getCurrentMonthYear();
                int particularMonth = monthYear[0];
                int particularYear = monthYear[1];

                String month = new DateFormatSymbols().getMonths()[particularMonth];
                String foodBudgetDate = String.valueOf(month+" "+particularYear);
                String foodBudgetTime = timeFormat.format(currentDateAndTime);
                String foodBudgetAmount = setAmount.getText().toString();

                if (foodBudgetAmount.equals("")){
                    setAmount.setError("Required");
                }else {
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {

                            BudgetDatabase database = BudgetDatabase.getInstance(getActivity());
                            Budget budget = database.budgetDao().getBudgetByMonthAndCategory(particularBGMonth, "Food");

                            if (budget == null) {
                                Budget newBudget = new Budget();
                                newBudget.setBudget_name("Food");
                                newBudget.setBudget_amount(Integer.parseInt(foodBudgetAmount));
                                newBudget.setBudget_date(foodBudgetDate);
                                newBudget.setBudget_time(foodBudgetTime);
                                newBudget.setBudget_timestamp(System.currentTimeMillis());

                                database.budgetDao().addBudget(newBudget);
                            } else {
                                budget.setBudget_id(budget.getBudget_id());
                                budget.setBudget_name("Food");
                                budget.setBudget_amount(Integer.parseInt(foodBudgetAmount));
                                budget.setBudget_date(budget.getBudget_date());
                                budget.setBudget_time(foodBudgetTime);
                                budget.setBudget_timestamp(budget.getBudget_timestamp());

                                database.budgetDao().updateBudget(budget);
                            }
                        }
                    });
                }
                ShowData();
                alertDialog.dismiss();
            });
            alertDialog.show();
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        });

        binding.setHousingBudget.setOnClickListener(view -> {
            final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            View dialog = LayoutInflater.from(getActivity()).inflate(R.layout.set_budget_layout, null);
            alert.setView(dialog);

            final AlertDialog alertDialog = alert.create();
            alertDialog.setCancelable(true);

            TextView title =  dialog.findViewById(R.id.setTitle);
            EditText setAmount = dialog.findViewById(R.id.setBudgetAmount);
            AppCompatButton setBtn = dialog.findViewById(R.id.setBudgetBtn);

            title.setText("Set Housing Budget");

            setBtn.setOnClickListener(view1 -> {

                Date currentDateAndTime = new Date();
                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());

                int[] monthYear = getCurrentMonthYear();
                int particularMonth = monthYear[0];
                int particularYear = monthYear[1];

                String month = new DateFormatSymbols().getMonths()[particularMonth];
                String housingBudgetDate = String.valueOf(month+" "+particularYear);
                String housingBudgetTime = timeFormat.format(currentDateAndTime);
                String housingBudgetAmount = setAmount.getText().toString();

                if (housingBudgetAmount.equals("")){
                    setAmount.setError("Required");
                }else {
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {

                            BudgetDatabase database = BudgetDatabase.getInstance(getActivity());
                            Budget budget = database.budgetDao().getBudgetByMonthAndCategory(particularBGMonth, "Housing");

                            if (budget == null) {
                                Budget newBudget = new Budget();
                                newBudget.setBudget_name("Housing");
                                newBudget.setBudget_amount(Integer.parseInt(housingBudgetAmount));
                                newBudget.setBudget_date(housingBudgetDate);
                                newBudget.setBudget_time(housingBudgetTime);
                                newBudget.setBudget_timestamp(System.currentTimeMillis());

                                database.budgetDao().addBudget(newBudget);
                            } else {
                                budget.setBudget_id(budget.getBudget_id());
                                budget.setBudget_name("Housing");
                                budget.setBudget_amount(Integer.parseInt(housingBudgetAmount));
                                budget.setBudget_date(budget.getBudget_date());
                                budget.setBudget_time(housingBudgetTime);
                                budget.setBudget_timestamp(budget.getBudget_timestamp());

                                database.budgetDao().updateBudget(budget);
                            }
                        }
                    });
                }
                ShowData();
                alertDialog.dismiss();
            });
            alertDialog.show();
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        });

        binding.setEducationBudget.setOnClickListener(view -> {
            final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            View dialog = LayoutInflater.from(getActivity()).inflate(R.layout.set_budget_layout, null);
            alert.setView(dialog);

            final AlertDialog alertDialog = alert.create();
            alertDialog.setCancelable(true);

            TextView title =  dialog.findViewById(R.id.setTitle);
            EditText setAmount = dialog.findViewById(R.id.setBudgetAmount);
            AppCompatButton setBtn = dialog.findViewById(R.id.setBudgetBtn);

            title.setText("Set Education Budget");

            setBtn.setOnClickListener(view1 -> {

                Date currentDateAndTime = new Date();
                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());

                int[] monthYear = getCurrentMonthYear();
                int particularMonth = monthYear[0];
                int particularYear = monthYear[1];

                String month = new DateFormatSymbols().getMonths()[particularMonth];
                String educationBudgetDate = String.valueOf(month+" "+particularYear);
                String educationBudgetTime = timeFormat.format(currentDateAndTime);
                String educationBudgetAmount = setAmount.getText().toString();

                if (educationBudgetAmount.equals("")){
                    setAmount.setError("Required");
                }else {
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {

                            BudgetDatabase database = BudgetDatabase.getInstance(getActivity());
                            Budget budget = database.budgetDao().getBudgetByMonthAndCategory(particularBGMonth, "Education");

                            if (budget == null) {
                                Budget newBudget = new Budget();
                                newBudget.setBudget_name("Education");
                                newBudget.setBudget_amount(Integer.parseInt(educationBudgetAmount));
                                newBudget.setBudget_date(educationBudgetDate);
                                newBudget.setBudget_time(educationBudgetTime);
                                newBudget.setBudget_timestamp(System.currentTimeMillis());

                                database.budgetDao().addBudget(newBudget);
                            } else {
                                budget.setBudget_id(budget.getBudget_id());
                                budget.setBudget_name("Education");
                                budget.setBudget_amount(Integer.parseInt(educationBudgetAmount));
                                budget.setBudget_date(budget.getBudget_date());
                                budget.setBudget_time(educationBudgetTime);
                                budget.setBudget_timestamp(budget.getBudget_timestamp());

                                database.budgetDao().updateBudget(budget);
                            }
                        }
                    });
                }
                ShowData();
                alertDialog.dismiss();
            });
            alertDialog.show();
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        });

        binding.setShoppingBudget.setOnClickListener(view -> {
            final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            View dialog = LayoutInflater.from(getActivity()).inflate(R.layout.set_budget_layout, null);
            alert.setView(dialog);

            final AlertDialog alertDialog = alert.create();
            alertDialog.setCancelable(true);

            TextView title =  dialog.findViewById(R.id.setTitle);
            EditText setAmount = dialog.findViewById(R.id.setBudgetAmount);
            AppCompatButton setBtn = dialog.findViewById(R.id.setBudgetBtn);

            title.setText("Set Shopping Budget");

            setBtn.setOnClickListener(view1 -> {

                Date currentDateAndTime = new Date();
                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());

                int[] monthYear = getCurrentMonthYear();
                int particularMonth = monthYear[0];
                int particularYear = monthYear[1];

                String month = new DateFormatSymbols().getMonths()[particularMonth];
                String shoppingBudgetDate = String.valueOf(month+" "+particularYear);
                String shoppingBudgetTime = timeFormat.format(currentDateAndTime);
                String shoppingBudgetAmount = setAmount.getText().toString();

                if (shoppingBudgetAmount.equals("")){
                    setAmount.setError("Required");
                }else {
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {

                            BudgetDatabase database = BudgetDatabase.getInstance(getActivity());
                            Budget budget = database.budgetDao().getBudgetByMonthAndCategory(particularBGMonth, "Shopping");

                            if (budget == null) {
                                Budget newBudget = new Budget();
                                newBudget.setBudget_name("Shopping");
                                newBudget.setBudget_amount(Integer.parseInt(shoppingBudgetAmount));
                                newBudget.setBudget_date(shoppingBudgetDate);
                                newBudget.setBudget_time(shoppingBudgetTime);
                                newBudget.setBudget_timestamp(System.currentTimeMillis());

                                database.budgetDao().addBudget(newBudget);
                            } else {
                                budget.setBudget_id(budget.getBudget_id());
                                budget.setBudget_name("Shopping");
                                budget.setBudget_amount(Integer.parseInt(shoppingBudgetAmount));
                                budget.setBudget_date(budget.getBudget_date());
                                budget.setBudget_time(shoppingBudgetTime);
                                budget.setBudget_timestamp(budget.getBudget_timestamp());

                                database.budgetDao().updateBudget(budget);
                            }
                        }
                    });
                }
                ShowData();
                alertDialog.dismiss();
            });
            alertDialog.show();
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        });

        binding.setTransportBudget.setOnClickListener(view -> {
            final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            View dialog = LayoutInflater.from(getActivity()).inflate(R.layout.set_budget_layout, null);
            alert.setView(dialog);

            final AlertDialog alertDialog = alert.create();
            alertDialog.setCancelable(true);

            TextView title =  dialog.findViewById(R.id.setTitle);
            EditText setAmount = dialog.findViewById(R.id.setBudgetAmount);
            AppCompatButton setBtn = dialog.findViewById(R.id.setBudgetBtn);

            title.setText("Set Transport Budget");

            setBtn.setOnClickListener(view1 -> {

                Date currentDateAndTime = new Date();
                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());

                int[] monthYear = getCurrentMonthYear();
                int particularMonth = monthYear[0];
                int particularYear = monthYear[1];

                String month = new DateFormatSymbols().getMonths()[particularMonth];
                String transportBudgetDate = String.valueOf(month+" "+particularYear);
                String transportBudgetTime = timeFormat.format(currentDateAndTime);
                String transportBudgetAmount = setAmount.getText().toString();

                if (transportBudgetAmount.equals("")){
                    setAmount.setError("Required");
                }else {
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {

                            BudgetDatabase database = BudgetDatabase.getInstance(getActivity());
                            Budget budget = database.budgetDao().getBudgetByMonthAndCategory(particularBGMonth, "Transport");

                            if (budget == null) {
                                Budget newBudget = new Budget();
                                newBudget.setBudget_name("Transport");
                                newBudget.setBudget_amount(Integer.parseInt(transportBudgetAmount));
                                newBudget.setBudget_date(transportBudgetDate);
                                newBudget.setBudget_time(transportBudgetTime);
                                newBudget.setBudget_timestamp(System.currentTimeMillis());

                                database.budgetDao().addBudget(newBudget);
                            } else {
                                budget.setBudget_id(budget.getBudget_id());
                                budget.setBudget_name("Transport");
                                budget.setBudget_amount(Integer.parseInt(transportBudgetAmount));
                                budget.setBudget_date(budget.getBudget_date());
                                budget.setBudget_time(transportBudgetTime);
                                budget.setBudget_timestamp(budget.getBudget_timestamp());

                                database.budgetDao().updateBudget(budget);
                            }
                        }
                    });
                }
                ShowData();
                alertDialog.dismiss();
            });
            alertDialog.show();
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        });

        binding.setUtilitiesBudget.setOnClickListener(view -> {
            final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            View dialog = LayoutInflater.from(getActivity()).inflate(R.layout.set_budget_layout, null);
            alert.setView(dialog);

            final AlertDialog alertDialog = alert.create();
            alertDialog.setCancelable(true);

            TextView title =  dialog.findViewById(R.id.setTitle);
            EditText setAmount = dialog.findViewById(R.id.setBudgetAmount);
            AppCompatButton setBtn = dialog.findViewById(R.id.setBudgetBtn);

            title.setText("Set Utilities Budget");

            setBtn.setOnClickListener(view1 -> {

                Date currentDateAndTime = new Date();
                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());

                int[] monthYear = getCurrentMonthYear();
                int particularMonth = monthYear[0];
                int particularYear = monthYear[1];

                String month = new DateFormatSymbols().getMonths()[particularMonth];
                String utilitiesBudgetDate = String.valueOf(month+" "+particularYear);
                String utilitiesBudgetTime = timeFormat.format(currentDateAndTime);
                String utilitiesBudgetAmount = setAmount.getText().toString();

                if (utilitiesBudgetAmount.equals("")){
                    setAmount.setError("Required");
                }else {
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {

                            BudgetDatabase database = BudgetDatabase.getInstance(getActivity());
                            Budget budget = database.budgetDao().getBudgetByMonthAndCategory(particularBGMonth, "Utilities");

                            if (budget == null) {
                                Budget newBudget = new Budget();
                                newBudget.setBudget_name("Utilities");
                                newBudget.setBudget_amount(Integer.parseInt(utilitiesBudgetAmount));
                                newBudget.setBudget_date(utilitiesBudgetDate);
                                newBudget.setBudget_time(utilitiesBudgetTime);
                                newBudget.setBudget_timestamp(System.currentTimeMillis());

                                database.budgetDao().addBudget(newBudget);
                            } else {
                                budget.setBudget_id(budget.getBudget_id());
                                budget.setBudget_name("Utilities");
                                budget.setBudget_amount(Integer.parseInt(utilitiesBudgetAmount));
                                budget.setBudget_date(budget.getBudget_date());
                                budget.setBudget_time(utilitiesBudgetTime);
                                budget.setBudget_timestamp(budget.getBudget_timestamp());

                                database.budgetDao().updateBudget(budget);
                            }
                        }
                    });
                }
                ShowData();
                alertDialog.dismiss();
            });
            alertDialog.show();
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        });

        ShowData();

        return binding.getRoot();
    }

    private void ShowData() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                BudgetDatabase database = BudgetDatabase.getInstance(getActivity());
                Budget foodBudget = database.budgetDao().getBudgetByMonthAndCategory(particularBGMonth, "Food");
                Budget housingBudget = database.budgetDao().getBudgetByMonthAndCategory(particularBGMonth, "Housing");
                Budget educationBudget = database.budgetDao().getBudgetByMonthAndCategory(particularBGMonth, "Education");
                Budget shoppingBudget = database.budgetDao().getBudgetByMonthAndCategory(particularBGMonth, "Shopping");
                Budget transportBudget = database.budgetDao().getBudgetByMonthAndCategory(particularBGMonth, "Transport");
                Budget utilitiesBudget = database.budgetDao().getBudgetByMonthAndCategory(particularBGMonth, "Utilities");

                ExpenseDatabase expenseDatabase = ExpenseDatabase.getInstance(getActivity());
                List<Expense> foodExpense = expenseDatabase.expenseDao().getAllExpenseByParticularMonthAndCategory(particularBGMonth, "Food");
                List<Expense> housingExpense = expenseDatabase.expenseDao().getAllExpenseByParticularMonthAndCategory(particularBGMonth, "Housing");
                List<Expense> educationExpense = expenseDatabase.expenseDao().getAllExpenseByParticularMonthAndCategory(particularBGMonth, "Education");
                List<Expense> shoppingExpense = expenseDatabase.expenseDao().getAllExpenseByParticularMonthAndCategory(particularBGMonth, "Shopping");
                List<Expense> transportExpense = expenseDatabase.expenseDao().getAllExpenseByParticularMonthAndCategory(particularBGMonth, "Transport");
                List<Expense> utilitiesExpense = expenseDatabase.expenseDao().getAllExpenseByParticularMonthAndCategory(particularBGMonth, "Utilities");

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        int foodConsume = 0;
                        if (foodExpense != null){
                            for (Expense expense: foodExpense){
                                foodConsume += expense.getExpense_amount();
                            }
                        }

                        int housingConsume = 0;
                        if (housingExpense != null){
                            for (Expense expense: housingExpense){
                                housingConsume += expense.getExpense_amount();
                            }
                        }

                        int educationConsume = 0;
                        if (educationExpense != null){
                            for (Expense expense: educationExpense){
                                educationConsume += expense.getExpense_amount();
                            }
                        }

                        int shoppingConsume = 0;
                        if (shoppingExpense != null){
                            for (Expense expense: shoppingExpense){
                                shoppingConsume += expense.getExpense_amount();
                            }
                        }

                        int transportConsume = 0;
                        if (transportExpense != null){
                            for (Expense expense: transportExpense){
                                transportConsume += expense.getExpense_amount();
                            }
                        }

                        int utilitiesConsume = 0;
                        if (utilitiesExpense != null){
                            for (Expense expense: utilitiesExpense){
                                utilitiesConsume += expense.getExpense_amount();
                            }
                        }

                        int totalBudget = 0;
                        if (foodBudget != null){
                            int budgetAmount = foodBudget.getBudget_amount();
                            int consumeAmount = foodConsume;
                            int remainingAmount = budgetAmount - consumeAmount;
                            float percent = ((float) consumeAmount / budgetAmount) * 100;
                            int percentValue = (int) percent;

                            binding.foodIndicator.setProgressCompat(percentValue, true);
                            binding.foodBudgetAmount.setText(String.valueOf(budgetAmount));
                            binding.foodConsumeAmount.setText(String.valueOf(consumeAmount));
                            binding.foodRemainingAmount.setText(String.valueOf(remainingAmount));
                            binding.foodPercentage.setText(String.valueOf(percentValue)+"%");

                            if (percentValue<=49){
                                binding.foodIndicator.setIndicatorColor(getResources().getColor(R.color.random2));
                            }else if (percentValue<=74){
                                binding.foodIndicator.setIndicatorColor(getResources().getColor(R.color.orange));
                            }else {
                                binding.foodIndicator.setIndicatorColor(getResources().getColor(R.color.tab_back));
                            }

                            totalBudget+=foodBudget.getBudget_amount();
                        }else {
                            binding.foodIndicator.setProgressCompat(0, true);
                            binding.foodBudgetAmount.setText("0");
                            binding.foodConsumeAmount.setText("0");
                            binding.foodRemainingAmount.setText("0");
                            binding.foodPercentage.setText("0%");
                        }

                        if (housingBudget != null){
                            int budgetAmount = housingBudget.getBudget_amount();
                            int consumeAmount = housingConsume;
                            int remainingAmount = budgetAmount - consumeAmount;
                            float percent = ((float) consumeAmount / budgetAmount) * 100;
                            int percentValue = (int) percent;

                            binding.housingIndicator.setProgressCompat(percentValue, true);
                            binding.housingBudgetAmount.setText(String.valueOf(budgetAmount));
                            binding.housingConsumeAmount.setText(String.valueOf(consumeAmount));
                            binding.housingRemainingAmount.setText(String.valueOf(remainingAmount));
                            binding.housingPercentage.setText(String.valueOf(percentValue)+"%");

                            if (percentValue<=49){
                                binding.housingIndicator.setIndicatorColor(getResources().getColor(R.color.random2));
                            }else if (percentValue<=74){
                                binding.housingIndicator.setIndicatorColor(getResources().getColor(R.color.orange));
                            }else {
                                binding.housingIndicator.setIndicatorColor(getResources().getColor(R.color.tab_back));
                            }

                            totalBudget+=housingBudget.getBudget_amount();
                        }else {
                            binding.housingIndicator.setProgressCompat(0, true);
                            binding.housingBudgetAmount.setText("0");
                            binding.housingConsumeAmount.setText("0");
                            binding.housingRemainingAmount.setText("0");
                            binding.housingPercentage.setText("0%");
                        }

                        if (educationBudget != null){
                            int budgetAmount = educationBudget.getBudget_amount();
                            int consumeAmount = educationConsume;
                            int remainingAmount = budgetAmount - consumeAmount;
                            float percent = ((float) consumeAmount / budgetAmount) * 100;
                            int percentValue = (int) percent;

                            binding.educationIndicator.setProgressCompat(percentValue, true);
                            binding.educationBudgetAmount.setText(String.valueOf(budgetAmount));
                            binding.educationConsumeAmount.setText(String.valueOf(consumeAmount));
                            binding.educationRemainingAmount.setText(String.valueOf(remainingAmount));
                            binding.educationPercentage.setText(String.valueOf(percentValue)+"%");

                            if (percentValue<=49){
                                binding.educationIndicator.setIndicatorColor(getResources().getColor(R.color.random2));
                            }else if (percentValue<=74){
                                binding.educationIndicator.setIndicatorColor(getResources().getColor(R.color.orange));
                            }else {
                                binding.educationIndicator.setIndicatorColor(getResources().getColor(R.color.tab_back));
                            }

                            totalBudget+=educationBudget.getBudget_amount();
                        }else {
                            binding.educationIndicator.setProgressCompat(0, true);
                            binding.educationBudgetAmount.setText("0");
                            binding.educationConsumeAmount.setText("0");
                            binding.educationRemainingAmount.setText("0");
                            binding.educationPercentage.setText("0%");
                        }

                        if (shoppingBudget != null){
                            int budgetAmount = shoppingBudget.getBudget_amount();
                            int consumeAmount = shoppingConsume;
                            int remainingAmount = budgetAmount - consumeAmount;
                            float percent = ((float) consumeAmount / budgetAmount) * 100;
                            int percentValue = (int) percent;

                            binding.shoppingIndicator.setProgressCompat(percentValue, true);
                            binding.shoppingBudgetAmount.setText(String.valueOf(budgetAmount));
                            binding.shoppingConsumeAmount.setText(String.valueOf(consumeAmount));
                            binding.shoppingRemainingAmount.setText(String.valueOf(remainingAmount));
                            binding.shoppingPercentage.setText(String.valueOf(percentValue)+"%");

                            if (percentValue<=49){
                                binding.shoppingIndicator.setIndicatorColor(getResources().getColor(R.color.random2));
                            }else if (percentValue<=74){
                                binding.shoppingIndicator.setIndicatorColor(getResources().getColor(R.color.orange));
                            }else {
                                binding.shoppingIndicator.setIndicatorColor(getResources().getColor(R.color.tab_back));
                            }

                            totalBudget+=shoppingBudget.getBudget_amount();
                        }else {
                            binding.shoppingIndicator.setProgressCompat(0, true);
                            binding.shoppingBudgetAmount.setText("0");
                            binding.shoppingConsumeAmount.setText("0");
                            binding.shoppingRemainingAmount.setText("0");
                            binding.shoppingPercentage.setText("0%");
                        }

                        if (transportBudget != null){
                            int budgetAmount = transportBudget.getBudget_amount();
                            int consumeAmount = transportConsume;
                            int remainingAmount = budgetAmount - consumeAmount;
                            float percent = ((float) consumeAmount / budgetAmount) * 100;
                            int percentValue = (int) percent;

                            binding.transportIndicator.setProgressCompat(percentValue, true);
                            binding.transportBudgetAmount.setText(String.valueOf(budgetAmount));
                            binding.transportConsumeAmount.setText(String.valueOf(consumeAmount));
                            binding.transportRemainingAmount.setText(String.valueOf(remainingAmount));
                            binding.transportPercentage.setText(String.valueOf(percentValue)+"%");

                            if (percentValue<=49){
                                binding.transportIndicator.setIndicatorColor(getResources().getColor(R.color.random2));
                            }else if (percentValue<=74){
                                binding.transportIndicator.setIndicatorColor(getResources().getColor(R.color.orange));
                            }else {
                                binding.transportIndicator.setIndicatorColor(getResources().getColor(R.color.tab_back));
                            }

                            totalBudget+=transportBudget.getBudget_amount();
                        }else {
                            binding.transportIndicator.setProgressCompat(0, true);
                            binding.transportBudgetAmount.setText("0");
                            binding.transportConsumeAmount.setText("0");
                            binding.transportRemainingAmount.setText("0");
                            binding.transportPercentage.setText("0%");
                        }

                        if (utilitiesBudget != null){
                            int budgetAmount = utilitiesBudget.getBudget_amount();
                            int consumeAmount = utilitiesConsume;
                            int remainingAmount = budgetAmount - consumeAmount;
                            float percent = ((float) consumeAmount / budgetAmount) * 100;
                            int percentValue = (int) percent;

                            binding.utilitiesIndicator.setProgressCompat(percentValue, true);
                            binding.utilitiesBudgetAmount.setText(String.valueOf(budgetAmount));
                            binding.utilitiesConsumeAmount.setText(String.valueOf(consumeAmount));
                            binding.utilitiesRemainingAmount.setText(String.valueOf(remainingAmount));
                            binding.utilitiesPercentage.setText(String.valueOf(percentValue)+"%");

                            if (percentValue<=49){
                                binding.utilitiesIndicator.setIndicatorColor(getResources().getColor(R.color.random2));
                            }else if (percentValue<=74){
                                binding.utilitiesIndicator.setIndicatorColor(getResources().getColor(R.color.orange));
                            }else {
                                binding.utilitiesIndicator.setIndicatorColor(getResources().getColor(R.color.tab_back));
                            }

                            totalBudget+=utilitiesBudget.getBudget_amount();
                        }else {
                            binding.utilitiesBudgetAmount.setText("00");
                            binding.utilitiesIndicator.setProgressCompat(0, true);
                            binding.utilitiesBudgetAmount.setText("0");
                            binding.utilitiesConsumeAmount.setText("0");
                            binding.utilitiesRemainingAmount.setText("0");
                            binding.utilitiesPercentage.setText("0%");
                        }
                        binding.totalBudget.setText(String.valueOf(totalBudget));
                    }
                });
            }
        });
    }


    private void refresh() {
        startActivity(new Intent(getActivity(), MainActivity.class));
    }

    private void updateMonthYear() {
        currentMonth = currentDate.get(Calendar.MONTH);
        currentYear = currentDate.get(Calendar.YEAR);
        String monthName = new DateFormatSymbols().getMonths()[currentMonth];
        binding.monthYearTextView.setText(monthName + " " + currentYear);
        particularBGMonth = binding.monthYearTextView.getText().toString();
    }
    private int[] getCurrentMonthYear() {
        Calendar currentDate = Calendar.getInstance();
        int particularMonth = currentDate.get(Calendar.MONTH);
        int particularYear = currentDate.get(Calendar.YEAR);
        return new int[]{particularMonth, particularYear};
    }
}