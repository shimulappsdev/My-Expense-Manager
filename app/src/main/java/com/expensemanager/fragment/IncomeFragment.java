package com.expensemanager.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.expensemanager.R;
import com.expensemanager.activity.MainActivity;
import com.expensemanager.adapter.IncomeAdapter;
import com.expensemanager.databinding.FragmentIncomeBinding;
import com.expensemanager.databse.IncomeDatabase;
import com.expensemanager.model.Income;
import com.expensemanager.utils.IncomeUpdateListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class IncomeFragment extends Fragment implements IncomeUpdateListener {

    FragmentIncomeBinding binding;
    IncomeAdapter incomeAdapter;
    String monthName;
    private Calendar currentDate = Calendar.getInstance();
    private int currentMonth;
    private int currentYear;
    String particularMonth;
    IncomeUpdateListener listener = this;
    Executor executor = Executors.newSingleThreadExecutor();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentIncomeBinding.inflate(getLayoutInflater(), container, false);

        binding.btnPreviousMonth.setOnClickListener(view -> {
            currentDate.add(Calendar.MONTH, -1);
            updateMonthYear();
            ShowData();
        });

        binding.btnNextMonth.setOnClickListener(view -> {
            currentDate.add(Calendar.MONTH, 1);
            updateMonthYear();
            ShowData();
        });

        updateMonthYear();

        binding.backBtn.setOnClickListener(view -> {
            backToPrevious();
        });

        binding.addIncomeBtn.getDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

        binding.addIncomeBtn.setOnClickListener(view -> {
            showBottomDialog();
        });

        ShowData();

        return binding.getRoot();
    }

    private void ShowData() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                IncomeDatabase database = IncomeDatabase.getInstance(getActivity());
                List<Income> incomeList = database.incomeDao().getIncomeByMonth(particularMonth);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        incomeAdapter = new IncomeAdapter(getActivity(), incomeList, listener);
                        binding.incomeRecyclerView.setAdapter(incomeAdapter);

                        int totalIncome = 0;
                        for (Income income : incomeList) {
                            totalIncome += income.getIncome_amount();
                        }
                        binding.totalIncome.setText(String.valueOf(totalIncome));

                        if (incomeList.size() == 0){
                            binding.noIncome.setVisibility(View.VISIBLE);
                        }else {
                            binding.noIncome.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
    }


    private void showBottomDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_income_layout);

        TextView insertIncome = dialog.findViewById(R.id.insertIncome);
        TextInputEditText incomeParticularName = dialog.findViewById(R.id.incomeParticularName);
        TextInputEditText incomeParticularAmount = dialog.findViewById(R.id.incomeParticularAmount);
        AutoCompleteTextView accountType = dialog.findViewById(R.id.accountType);

        String[] type = new String[]{"Bank", "Cash", "Bkash", "Nagad", "Rocket", "mCash", "Ucash", "SureCash", "T-Cash", "Ok Wallet", "Others"};

        ArrayAdapter adapter = new ArrayAdapter<>(
                getContext(),
                R.layout.account_type_list,
                type);

        accountType.setAdapter(adapter);

        TextInputLayout customType;
        customType = dialog.findViewById(R.id.customType);

        accountType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getContext(), type[i], Toast.LENGTH_SHORT).show();

                if (type[i].equals("Others")){
                    customType.setVisibility(View.VISIBLE);
                }else {
                    customType.setVisibility(View.GONE);
                }

            }
        });

        insertIncome.setOnClickListener(view -> {
            Date currentDateAndTime = new Date();
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String showData = dateFormat.format(currentDateAndTime);

            int[] monthYear = getCurrentMonthYear();
            int particularMonth = monthYear[0];
            int particularYear = monthYear[1];
            String month = new DateFormatSymbols().getMonths()[particularMonth];

            String incomeParticularDate = String.valueOf(month+" "+particularYear);
            String incomeParticularTime = timeFormat.format(currentDateAndTime);

            String particularName = incomeParticularName.getText().toString().trim();
            String particularAmount = incomeParticularAmount.getText().toString().trim();
            String particularAccountType = accountType.getText().toString().trim();

            if (particularName.equals("")){
                incomeParticularName.setError("Required");
            }else if (particularAmount.equals("")){
                incomeParticularAmount.setError("Required");
            }else if (particularAccountType.equals("")){
                accountType.setError("Please Select");
            }else {
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        IncomeDatabase database = IncomeDatabase.getInstance(getActivity());

                        Income addToIncome = new Income();
                        addToIncome.setIncome_particular(particularName);
                        addToIncome.setIncome_amount(Integer.parseInt(particularAmount));
                        addToIncome.setIncome_account_type(particularAccountType);
                        addToIncome.setIncome_date(incomeParticularDate);
                        addToIncome.setIncome_time(incomeParticularTime);
                        addToIncome.setShow_date(showData);
                        addToIncome.setIncome_timestamp(System.currentTimeMillis());

                        database.incomeDao().addIncome(addToIncome);
                    }
                });
            }
            ShowData();
            dialog.dismiss();
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void backToPrevious() {
        startActivity(new Intent(getActivity(), MainActivity.class));
    }

    private void updateMonthYear() {
        currentMonth = currentDate.get(Calendar.MONTH);
        currentYear = currentDate.get(Calendar.YEAR);
        monthName = new DateFormatSymbols().getMonths()[currentMonth];
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
    public void incomeItemEdit(Income income) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.edit_income_layout);

        TextView updateIncome = dialog.findViewById(R.id.updateIncome);
        TextInputEditText incomeParticularName = dialog.findViewById(R.id.incomeParticularName);
        TextInputEditText incomeParticularAmount = dialog.findViewById(R.id.incomeParticularAmount);
        AutoCompleteTextView accountType = dialog.findViewById(R.id.accountType);

        incomeParticularName.setText(income.getIncome_particular());
        incomeParticularAmount.setText(String.valueOf(income.getIncome_amount()));
        accountType.setText(income.getIncome_account_type());

        String[] type = new String[]{"Bank", "Cash", "Bkash", "Nagad", "Rocket", "mCash", "Ucash", "SureCash", "T-Cash", "Ok Wallet", "Others"};

        ArrayAdapter adapter = new ArrayAdapter<>(
                getActivity(),
                R.layout.account_type_list,
                type);

        accountType.setAdapter(adapter);

        TextInputLayout customType;
        customType = dialog.findViewById(R.id.customType);

        accountType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(), type[i], Toast.LENGTH_SHORT).show();

                if (type[i].equals("Others")){
                    customType.setVisibility(View.VISIBLE);
                }else {
                    customType.setVisibility(View.GONE);
                }

            }
        });

        updateIncome.setOnClickListener(view -> {
            String particularName = incomeParticularName.getText().toString().trim();
            String particularAmount = incomeParticularAmount.getText().toString().trim();
            String particularAccountType = accountType.getText().toString().trim();

            if (particularName.equals("")){
                incomeParticularName.setError("Required");
            }else if (particularAmount.equals("")){
                incomeParticularAmount.setError("Required");
            }else if (particularAccountType.equals("")){
                accountType.setError("Please Select");
            }else {
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        IncomeDatabase database = IncomeDatabase.getInstance(getActivity());
                        Income update = new Income();
                        update.setIncome_id(income.getIncome_id());
                        update.setIncome_particular(particularName);
                        update.setIncome_amount(Integer.parseInt(particularAmount));
                        update.setIncome_account_type(particularAccountType);
                        update.setIncome_date(income.getIncome_date());
                        update.setIncome_time(income.getIncome_time());
                        update.setShow_date(income.getShow_date());
                        update.setIncome_timestamp(income.getIncome_timestamp());

                        database.incomeDao().updateIncome(update);
                    }
                });
                ShowData();
            }
            dialog.dismiss();
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    @Override
    public void incomeItemDelete(Income income) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                IncomeDatabase database = IncomeDatabase.getInstance(getActivity());
                database.incomeDao().deleteIncome(income);
            }
        });
        ShowData();
    }
}