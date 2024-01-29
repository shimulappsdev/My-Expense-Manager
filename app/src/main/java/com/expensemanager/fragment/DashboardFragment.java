package com.expensemanager.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.expensemanager.R;
import com.expensemanager.activity.ContainerActivity;
import com.expensemanager.databinding.FragmentDashboardBinding;
import com.expensemanager.databse.BudgetDatabase;
import com.expensemanager.databse.ExpenseDatabase;
import com.expensemanager.databse.IncomeDatabase;
import com.expensemanager.databse.UserDatabase;
import com.expensemanager.model.Budget;
import com.expensemanager.model.Expense;
import com.expensemanager.model.Income;
import com.expensemanager.model.User;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DashboardFragment extends Fragment {

    FragmentDashboardBinding binding;
    NotificationManagerCompat managerCompat;
    NotificationCompat.Builder builder;
    String monthName, today;
    private Calendar currentDate = Calendar.getInstance();
    private int currentMonth;
    private int currentYear;
    private int todayExpenseAmount=0;
    String particularMonth, imageUrl;
    Executor executor = Executors.newSingleThreadExecutor();

    List<String> months = Arrays.asList("JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(getLayoutInflater(), container, false);


        ShowData();

        binding.profileImg.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ContainerActivity.class);
            intent.putExtra("profileImg", imageUrl);
            intent.putExtra("imageView", "imageView");
            startActivity(intent);
        });

        return binding.getRoot();
    }


    private void ShowData() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                updateMonthYear();
                IncomeDatabase incomeDatabase = IncomeDatabase.getInstance(getActivity());
                List<Income> incomeList = incomeDatabase.incomeDao().getIncomeByMonth(particularMonth);
                BudgetDatabase budgetDatabase = BudgetDatabase.getInstance(getActivity());
                List<Budget> budgetList = budgetDatabase.budgetDao().getAllBudgetByMonth(particularMonth);
                ExpenseDatabase expenseDatabase = ExpenseDatabase.getInstance(getActivity());
                List<Expense> expenseList = expenseDatabase.expenseDao().getAllExpenseByMonth(particularMonth);

                List<Expense> foodExpense = expenseDatabase.expenseDao().getAllExpenseByParticularMonthAndCategory(particularMonth, "Food");
                List<Expense> housingExpense = expenseDatabase.expenseDao().getAllExpenseByParticularMonthAndCategory(particularMonth, "Housing");
                List<Expense> educationExpense = expenseDatabase.expenseDao().getAllExpenseByParticularMonthAndCategory(particularMonth, "Education");
                List<Expense> shoppingExpense = expenseDatabase.expenseDao().getAllExpenseByParticularMonthAndCategory(particularMonth, "Shopping");
                List<Expense> transportExpense = expenseDatabase.expenseDao().getAllExpenseByParticularMonthAndCategory(particularMonth, "Transport");
                List<Expense> utilitiesExpense = expenseDatabase.expenseDao().getAllExpenseByParticularMonthAndCategory(particularMonth, "Utilities");

                List<Expense> januaryExpense = expenseDatabase.expenseDao().getAllExpenseByEachMonth("January");
                List<Expense> februaryExpense = expenseDatabase.expenseDao().getAllExpenseByEachMonth("February");
                List<Expense> marchExpense = expenseDatabase.expenseDao().getAllExpenseByEachMonth("March");
                List<Expense> aprilExpense = expenseDatabase.expenseDao().getAllExpenseByEachMonth("April");
                List<Expense> mayExpense = expenseDatabase.expenseDao().getAllExpenseByEachMonth("May");
                List<Expense> juneExpense = expenseDatabase.expenseDao().getAllExpenseByEachMonth("June");
                List<Expense> julyExpense = expenseDatabase.expenseDao().getAllExpenseByEachMonth("July");
                List<Expense> augustExpense = expenseDatabase.expenseDao().getAllExpenseByEachMonth("August");
                List<Expense> septemberExpense = expenseDatabase.expenseDao().getAllExpenseByEachMonth("September");
                List<Expense> octoberExpense = expenseDatabase.expenseDao().getAllExpenseByEachMonth("October");
                List<Expense> novemberExpense = expenseDatabase.expenseDao().getAllExpenseByEachMonth("November");
                List<Expense> decemberExpense = expenseDatabase.expenseDao().getAllExpenseByEachMonth("December");

                User userData = UserDatabase.getInstance(getActivity()).userDao().getUser();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (userData != null){
                            binding.profileName.setText(userData.getUser_name());
                            if (!userData.getUser_profile_img().equals("")){
                                binding.profileImg.setImageURI(Uri.parse(userData.getUser_profile_img()));
                                imageUrl = userData.getUser_profile_img();
                            }
                        }

                        int totalIncome = 0;
                        if (incomeList != null){
                            for (Income income : incomeList) {
                                totalIncome += income.getIncome_amount();
                            }
                            binding.incomeAmount.setText(String.valueOf(totalIncome));
                        }
                        if (budgetList != null){
                            int totalBudget = 0;
                            for (Budget budget : budgetList) {
                                totalBudget += budget.getBudget_amount();
                            }
                            binding.budgetAmount.setText(String.valueOf(totalBudget));
                        }
                        int totalExpense = 0;
                        if (expenseList != null){
                            for (Expense expense : expenseList) {
                                totalExpense += expense.getExpense_amount();
                            }

                            int foodExpenseAmount = 0;
                            if (foodExpense != null){
                                for (Expense expense : foodExpense){
                                    foodExpenseAmount += expense.getExpense_amount();
                                }
                            }

                            int housingExpenseAmount = 0;
                            if (housingExpense != null){
                                for (Expense expense : housingExpense){
                                    housingExpenseAmount += expense.getExpense_amount();
                                }
                            }

                            int educationExpenseAmount = 0;
                            if (educationExpense != null){
                                for (Expense expense : educationExpense){
                                    educationExpenseAmount += expense.getExpense_amount();
                                }
                            }

                            int shoppingExpenseAmount = 0;
                            if (shoppingExpense != null){
                                for (Expense expense : shoppingExpense){
                                    shoppingExpenseAmount += expense.getExpense_amount();
                                }
                            }

                            int transportExpenseAmount = 0;
                            if (transportExpense != null){
                                for (Expense expense : transportExpense){
                                    transportExpenseAmount += expense.getExpense_amount();
                                }
                            }

                            int utilitiesExpenseAmount = 0;
                            if (utilitiesExpense != null){
                                for (Expense expense : utilitiesExpense){
                                    utilitiesExpenseAmount += expense.getExpense_amount();
                                }
                            }

                            int [] amount = {foodExpenseAmount, housingExpenseAmount, educationExpenseAmount, shoppingExpenseAmount, transportExpenseAmount, utilitiesExpenseAmount};
                            String[] category = {"Food", "Housing", "Education", "Shopping", "Transport", "Utilities"};

                            ArrayList<PieEntry> pieEntries = new ArrayList<>();
                            for (int i = 0; i < amount.length; i++) {
                                if (amount[i] == 0){
                                    continue;
                                }
                                pieEntries.add(new PieEntry(amount[i], category[i]));
                            }

                            if (foodExpenseAmount == 0 && housingExpenseAmount == 0 && educationExpenseAmount == 0 && shoppingExpenseAmount == 0 && transportExpenseAmount == 0 && utilitiesExpenseAmount == 0){
                                pieEntries.add(new PieEntry(1, "Set Expense"));
                                pieEntries.add(new PieEntry(1, "Set Expense"));
                                pieEntries.add(new PieEntry(1, "Set Expense"));
                                pieEntries.add(new PieEntry(1, "Set Expense"));
                                pieEntries.add(new PieEntry(1, "Set Expense"));
                                pieEntries.add(new PieEntry(1, "Set Expense"));
                            }

                            PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
                            pieDataSet.setColors(customColors);
                            pieDataSet.setValueTextColor(Color.WHITE);
                            pieDataSet.setValueTextSize(8f);
                            PieData pieData = new PieData(pieDataSet);
                            binding.expenseChart.setData(pieData);
                            binding.expenseChart.getDescription().setEnabled(false);
                            binding.expenseChart.setCenterText("Expense\n"+totalExpense);
                            binding.expenseChart.setCenterTextColor(getResources().getColor(R.color.theme_color));
                            binding.expenseChart.setCenterTextSize(14f);
                            binding.expenseChart.animateY(1500);
                            binding.expenseChart.notifyDataSetChanged();
                            binding.expenseChart.setDrawEntryLabels(false);
                            binding.expenseChart.setUsePercentValues(true);
                            binding.expenseChart.setHoleRadius(40f);
                            binding.expenseChart.setTransparentCircleRadius(45f);

                            ValueFormatter percentFormatter = new PercentValueFormatter();

                            PieData pieData1 = binding.expenseChart.getData();
                            PieDataSet dataSet1 = (PieDataSet) pieData1.getDataSetByIndex(0);
                            dataSet1.setValueFormatter(percentFormatter);

                            Legend legend = binding.expenseChart.getLegend();
                            legend.setEnabled(false);
                            createCustomLegend(pieEntries);
                            binding.expenseChart.invalidate();


                            int januaryExpenseAmount = 0;
                            if (januaryExpense != null){
                                for (Expense expense : januaryExpense){
                                    januaryExpenseAmount += expense.getExpense_amount();
                                }
                            }

                            int februaryExpenseAmount = 0;
                            if (februaryExpense != null){
                                for (Expense expense : februaryExpense){
                                    februaryExpenseAmount += expense.getExpense_amount();
                                }
                            }

                            int marchExpenseAmount = 0;
                            if (marchExpense != null){
                                for (Expense expense : marchExpense){
                                    marchExpenseAmount += expense.getExpense_amount();
                                }
                            }

                            int aprilExpenseAmount = 0;
                            if (aprilExpense != null){
                                for (Expense expense : aprilExpense){
                                    aprilExpenseAmount += expense.getExpense_amount();
                                }
                            }

                            int mayExpenseAmount = 0;
                            if (mayExpense != null){
                                for (Expense expense : mayExpense){
                                    mayExpenseAmount += expense.getExpense_amount();
                                }
                            }

                            int juneExpenseAmount = 0;
                            if (juneExpense != null){
                                for (Expense expense : juneExpense){
                                    juneExpenseAmount += expense.getExpense_amount();
                                }
                            }

                            int julyExpenseAmount = 0;
                            if (julyExpense != null){
                                for (Expense expense : julyExpense){
                                    julyExpenseAmount += expense.getExpense_amount();
                                }
                            }

                            int augustExpenseAmount = 0;
                            if (augustExpense != null){
                                for (Expense expense : augustExpense){
                                    augustExpenseAmount += expense.getExpense_amount();
                                }
                            }

                            int septemberExpenseAmount = 0;
                            if (septemberExpense != null){
                                for (Expense expense : septemberExpense){
                                    septemberExpenseAmount += expense.getExpense_amount();
                                }
                            }

                            int octoberExpenseAmount = 0;
                            if (octoberExpense != null){
                                for (Expense expense : octoberExpense){
                                    octoberExpenseAmount += expense.getExpense_amount();
                                }
                            }

                            int novemberExpenseAmount = 0;
                            if (novemberExpense != null){
                                for (Expense expense : novemberExpense){
                                    novemberExpenseAmount += expense.getExpense_amount();
                                }
                            }

                            int decemberExpenseAmount = 0;
                            if (decemberExpense != null){
                                for (Expense expense : decemberExpense){
                                    decemberExpenseAmount += expense.getExpense_amount();
                                }
                            }

                            binding.monthExpense.getAxisRight().setDrawLabels(false);
                            ArrayList<BarEntry> entries = new ArrayList<>();
                            if (januaryExpenseAmount == 0 && februaryExpenseAmount == 0 && marchExpenseAmount == 0 && aprilExpenseAmount == 0 && mayExpenseAmount == 0 && juneExpenseAmount == 0 && julyExpenseAmount == 0 && augustExpenseAmount == 0 && septemberExpenseAmount == 0 && octoberExpenseAmount == 0 && novemberExpenseAmount == 0 && decemberExpenseAmount == 0){
                                entries.add(new BarEntry(0, 1));
                                entries.add(new BarEntry(1, 2));
                                entries.add(new BarEntry(2, 3));
                                entries.add(new BarEntry(3, 1));
                                entries.add(new BarEntry(4, 2));
                                entries.add(new BarEntry(5, 3));
                                entries.add(new BarEntry(6, 1));
                                entries.add(new BarEntry(7, 2));
                                entries.add(new BarEntry(8, 3));
                                entries.add(new BarEntry(9, 1));
                                entries.add(new BarEntry(10, 2));
                                entries.add(new BarEntry(11, 3));
                            }else {
                                entries.add(new BarEntry(0, januaryExpenseAmount));
                                entries.add(new BarEntry(1, februaryExpenseAmount));
                                entries.add(new BarEntry(2, marchExpenseAmount));
                                entries.add(new BarEntry(3, aprilExpenseAmount));
                                entries.add(new BarEntry(4, mayExpenseAmount));
                                entries.add(new BarEntry(5, juneExpenseAmount));
                                entries.add(new BarEntry(6, julyExpenseAmount));
                                entries.add(new BarEntry(7, augustExpenseAmount));
                                entries.add(new BarEntry(8, septemberExpenseAmount));
                                entries.add(new BarEntry(9, octoberExpenseAmount));
                                entries.add(new BarEntry(10, novemberExpenseAmount));
                                entries.add(new BarEntry(11, decemberExpenseAmount));
                            }

                            BarDataSet dataSet = new BarDataSet(entries, "");
                            dataSet.setColors(customColors);
                            BarData barData = new BarData(dataSet);
                            binding.monthExpense.setData(barData);
                            binding.monthExpense.getDescription().setEnabled(false);
                            binding.monthExpense.invalidate();
                            binding.monthExpense.animateY(1500);
                            binding.monthExpense.getXAxis().setValueFormatter(new IndexAxisValueFormatter(months));
                            binding.monthExpense.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                            binding.monthExpense.getXAxis().setGranularity(1f);
                            binding.monthExpense.getXAxis().setGranularityEnabled(true);

                            Legend legend2 = binding.monthExpense.getLegend();
                            legend2.setEnabled(false);

                        }

                        binding.balanceAmount.setText(String.valueOf(totalIncome - totalExpense));
                    }
                });
            }
        });
    }

    private void createCustomLegend(ArrayList<PieEntry> entries) {
        for (int i = 0; i < entries.size(); i++) {
            PieEntry entry = entries.get(i);
            String label = entry.getLabel();

            LinearLayout entryLayout = new LinearLayout(getActivity());
            entryLayout.setOrientation(LinearLayout.HORIZONTAL);
            entryLayout.setGravity(Gravity.CENTER_VERTICAL);

            View colorIndicator = new View(getActivity());
            int color = customColors[i % customColors.length];
            colorIndicator.setBackgroundColor(color);
            int colorSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, getResources().getDisplayMetrics());
            LinearLayout.LayoutParams colorParams = new LinearLayout.LayoutParams(colorSize, colorSize);
            colorParams.setMarginEnd(10);
            colorIndicator.setLayoutParams(colorParams);

            TextView textView = new TextView(getActivity());
            textView.setText(label);
            textView.setTextColor(Color.BLACK);
            textView.setTextSize(12);

            entryLayout.addView(colorIndicator);
            entryLayout.addView(textView);

            LinearLayout.LayoutParams entryParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            entryParams.setMargins(2, 4, 2, 4);
            entryLayout.setLayoutParams(entryParams);

            binding.legendLayout.addView(entryLayout);
            binding.legendLayout.setOrientation(LinearLayout.VERTICAL);
            binding.legendLayout.setGravity(Gravity.CENTER_VERTICAL);
        }
    }

    public class PercentValueFormatter extends ValueFormatter {

            @Override
            public String getFormattedValue(float value) {
                return String.format("%.1f%%", value);
            }
        }

    private void updateMonthYear() {
        currentMonth = currentDate.get(Calendar.MONTH);
        currentYear = currentDate.get(Calendar.YEAR);
        monthName = new DateFormatSymbols().getMonths()[currentMonth];
        particularMonth = monthName + " " + currentYear;
    }

    private int[] customColors = new int[]{
            Color.parseColor("#FFC300"),
            Color.parseColor("#79AC78"),
            Color.parseColor("#C70039"),
            Color.parseColor("#5B0888"),
            Color.parseColor("#3D0C11"),
            Color.parseColor("#4477CE")
    };

}