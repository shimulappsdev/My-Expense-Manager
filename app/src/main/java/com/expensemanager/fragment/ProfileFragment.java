package com.expensemanager.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.expensemanager.R;
import com.expensemanager.activity.ContainerActivity;
import com.expensemanager.activity.EntryActivity;
import com.expensemanager.activity.MainActivity;
import com.expensemanager.adapter.IncomeAdapter;
import com.expensemanager.databinding.FragmentProfileBinding;
import com.expensemanager.databse.BudgetDatabase;
import com.expensemanager.databse.ExpenseDatabase;
import com.expensemanager.databse.IncomeDatabase;
import com.expensemanager.databse.UserDatabase;
import com.expensemanager.model.Budget;
import com.expensemanager.model.Expense;
import com.expensemanager.model.Income;
import com.expensemanager.model.User;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ProfileFragment extends Fragment {

    FragmentProfileBinding binding;
    Executor executor = Executors.newSingleThreadExecutor();
    String imageUrl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(getLayoutInflater(), container, false);


        ShowData();

        binding.settingBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ContainerActivity.class);
            intent.putExtra("setting","setting");
            startActivity(intent);
        });

        binding.incomeHistoryBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ContainerActivity.class);
            intent.putExtra("incomeHistory", "incomeHistory");
            startActivity(intent);
        });

        binding.expenseHistoryBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ContainerActivity.class);
            intent.putExtra("expenseHistory", "expenseHistory");
            startActivity(intent);
        });

        binding.budgetHistoryBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ContainerActivity.class);
            intent.putExtra("budgetHistory", "budgetHistory");
            startActivity(intent);
        });

        binding.logoutBtn.setOnClickListener(view -> {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("isLoggedIn", false);
            editor.apply();

            Intent intent = new Intent(new Intent(getActivity(), EntryActivity.class));
            intent.putExtra("login", "login");
            startActivity(intent);
            getActivity().finish();
        });

        binding.profileImg.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ContainerActivity.class);
            intent.putExtra("profileImg", imageUrl);
            intent.putExtra("imageView", "imageView");
            startActivity(intent);
        });

        binding.backBtn.setOnClickListener(view -> {
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        });

        return binding.getRoot();
    }

    private void ShowData() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                IncomeDatabase incomeDatabase = IncomeDatabase.getInstance(getActivity());
                BudgetDatabase budgetDatabase = BudgetDatabase.getInstance(getActivity());
                ExpenseDatabase expenseDatabase = ExpenseDatabase.getInstance(getActivity());
                UserDatabase userDatabase = UserDatabase.getInstance(getActivity());

                List<Income> incomeList = incomeDatabase.incomeDao().getAllIncome();
                List<Budget> budgetList = budgetDatabase.budgetDao().getAllBudget();
                List<Expense> expenseList = expenseDatabase.expenseDao().getAllExpense();
                User user = userDatabase.userDao().getUser();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (incomeList != null){
                            int totalIncome = 0;
                            for (Income income : incomeList) {
                                totalIncome += income.getIncome_amount();
                            }
                            binding.totalIncome.setText(String.valueOf(totalIncome));
                        }

                        if (budgetList != null){
                            int totalBudget = 0;
                            for (Budget budget : budgetList) {
                                totalBudget += budget.getBudget_amount();
                            }
                            binding.totalBudget.setText(String.valueOf(totalBudget));
                        }

                        if (expenseList != null){
                            int totalExpense = 0;
                            for (Expense expense : expenseList) {
                                totalExpense += expense.getExpense_amount();
                            }
                            binding.totalExpense.setText(String.valueOf(totalExpense));
                        }
                        if (user != null){
                            binding.profileName.setText(user.getUser_name());
                            binding.profileProfession.setText(user.getUser_profession());
                            binding.profileEmail.setText(user.getUser_email());
                            binding.profileMobile.setText(user.getUser_phone());
                            binding.profilePassword.setText(user.getUser_password());
                            if (!user.getUser_profile_img().equals("")){
                                binding.profileImg.setImageURI(Uri.parse(user.getUser_profile_img()));
                                imageUrl = user.getUser_profile_img();
                            }
                        }
                    }
                });
            }
        });

    }
}