package com.expensemanager.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.expensemanager.R;
import com.expensemanager.databinding.ActivityContainerBinding;
import com.expensemanager.fragment.BudgetHistoryFragment;
import com.expensemanager.fragment.EditListFragment;
import com.expensemanager.fragment.ExpenseHistoryFragment;
import com.expensemanager.fragment.ImageViewFragment;
import com.expensemanager.fragment.IncomeHistoryFragment;
import com.expensemanager.fragment.ListFragment;
import com.expensemanager.fragment.ListViewFragment;
import com.expensemanager.fragment.PreparingFragment;
import com.expensemanager.fragment.ProfileFragment;
import com.expensemanager.fragment.SettingFragment;

public class ContainerActivity extends AppCompatActivity {

    ActivityContainerBinding binding;
    ListFragment listFragment = new ListFragment();
    ListViewFragment listViewFragment = new ListViewFragment();
    PreparingFragment preparingFragment = new PreparingFragment();
    ProfileFragment profileFragment = new ProfileFragment();
    SettingFragment settingFragment = new SettingFragment();
    IncomeHistoryFragment incomeHistoryFragment = new IncomeHistoryFragment();
    ExpenseHistoryFragment expenseHistoryFragment = new ExpenseHistoryFragment();
    BudgetHistoryFragment budgetHistoryFragment = new BudgetHistoryFragment();
    EditListFragment editListFragment = new EditListFragment();
    ImageViewFragment imageViewFragment = new ImageViewFragment();

    String list, listView, preparingList, listEdit, profile, setting, incomeHistory, expenseHistory, budgetHistory, editList, imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContainerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        list = getIntent().getStringExtra("list");
        listView = getIntent().getStringExtra("listView");
        preparingList = getIntent().getStringExtra("preparingList");
        listEdit = getIntent().getStringExtra("listEdit");
        profile = getIntent().getStringExtra("profile");
        setting = getIntent().getStringExtra("setting");
        incomeHistory = getIntent().getStringExtra("incomeHistory");
        expenseHistory = getIntent().getStringExtra("expenseHistory");
        budgetHistory = getIntent().getStringExtra("budgetHistory");
        editList = getIntent().getStringExtra("editList");
        imageView = getIntent().getStringExtra("imageView");

        if (list != null && list.equals("list")){
            getSupportFragmentManager().beginTransaction().replace(R.id.containerLayout, listFragment).commit();
        }

        if (listView != null && listView.equals("listView")){
            getSupportFragmentManager().beginTransaction().replace(R.id.containerLayout, listViewFragment).commit();
        }

        if (preparingList != null && preparingList.equals("preparingList")){
            getSupportFragmentManager().beginTransaction().replace(R.id.containerLayout, preparingFragment).commit();
        }

        if (listEdit != null && listEdit.equals("listEdit")){
            getSupportFragmentManager().beginTransaction().replace(R.id.containerLayout, preparingFragment).commit();
        }

        if (profile != null && profile.equals("profile")){
            getSupportFragmentManager().beginTransaction().replace(R.id.containerLayout, profileFragment).commit();
        }

        if (setting != null && setting.equals("setting")){
            getSupportFragmentManager().beginTransaction().replace(R.id.containerLayout, settingFragment).commit();
        }

        if (incomeHistory != null && incomeHistory.equals("incomeHistory")){
            getSupportFragmentManager().beginTransaction().replace(R.id.containerLayout, incomeHistoryFragment).commit();
        }

        if (expenseHistory != null && expenseHistory.equals("expenseHistory")){
            getSupportFragmentManager().beginTransaction().replace(R.id.containerLayout, expenseHistoryFragment).commit();
        }

        if (budgetHistory != null && budgetHistory.equals("budgetHistory")){
            getSupportFragmentManager().beginTransaction().replace(R.id.containerLayout, budgetHistoryFragment).commit();
        }

        if (editList != null && editList.equals("editList")){
            getSupportFragmentManager().beginTransaction().replace(R.id.containerLayout, editListFragment).commit();
        }

        if (imageView != null && imageView.equals("imageView")){
            getSupportFragmentManager().beginTransaction().replace(R.id.containerLayout, imageViewFragment).commit();
        }

    }
}