package com.expensemanager.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.expensemanager.R;
import com.expensemanager.activity.ContainerActivity;
import com.expensemanager.activity.MainActivity;
import com.expensemanager.adapter.FragmentNewAdapter;
import com.expensemanager.databinding.FragmentExpenseBinding;
import com.expensemanager.databse.ExpenseDatabase;
import com.expensemanager.model.Expense;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ExpenseFragment extends Fragment {

    FragmentExpenseBinding binding;

    FragmentNewAdapter fragmentsAdapter;
    FragmentManager fragmentManager;
    Executor executor = Executors.newSingleThreadExecutor();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentExpenseBinding.inflate(getLayoutInflater(), container, false);


        fragmentManager = getFragmentManager();
        fragmentsAdapter = new FragmentNewAdapter(fragmentManager);
        binding.viewPager.setAdapter(fragmentsAdapter);
        binding.viewPager.setOffscreenPageLimit(0);
        binding.tablayout.setupWithViewPager(binding.viewPager);

        binding.backBtn.setOnClickListener(view -> {
            Back();
        });

        binding.listBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ContainerActivity.class);
            intent.putExtra("list", "list");
            startActivity(intent);
        });

        binding.addExpenseBtn.getDrawable().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
        binding.listBtn.getDrawable().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);

        binding.addExpenseBtn.setOnClickListener(view -> {
            showTopDialog();
        });

        return binding.getRoot();
    }

    private void showTopDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_expense_layout);

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

        insertExpense.setOnClickListener(view -> {
            Date currentDateAndTime = new Date();
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String showData = dateFormat.format(currentDateAndTime);

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
                        addExpense.setExpense_particular(expenseName);
                        addExpense.setExpense_amount(Integer.parseInt(expenseAmount));
                        addExpense.setExpense_category(expenseCategory);
                        addExpense.setExpense_date(expenseParticularDate);
                        addExpense.setExpense_time(expenseParticularTime);
                        addExpense.setExpense_month(month);
                        addExpense.setShow_date(showData);
                        addExpense.setExpense_timeStamp(System.currentTimeMillis());

                        database.expenseDao().addExpense(addExpense);
                    }
                });
                replaceFragment(new ExpenseFragment());
            }
            dialog.dismiss();
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.ExpenseDialogAnimation;
        dialog.getWindow().setGravity(Gravity.TOP);

    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    private int[] getCurrentMonthYear() {
        Calendar currentDate = Calendar.getInstance();
        int particularMonth = currentDate.get(Calendar.MONTH); // Adding 1 to get 1-based month
        int particularYear = currentDate.get(Calendar.YEAR);
        return new int[]{particularMonth, particularYear};
    }

    private void Back() {
        startActivity(new Intent(getActivity(), MainActivity.class));
    }


}