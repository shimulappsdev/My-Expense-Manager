package com.expensemanager.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.expensemanager.R;
import com.expensemanager.databinding.ActivityEntryBinding;
import com.expensemanager.fragment.LogInFragment;
import com.expensemanager.fragment.RegisterFragment;
import com.expensemanager.fragment.SplashFragment;

public class EntryActivity extends AppCompatActivity {

    ActivityEntryBinding binding;
    SplashFragment splashFragment = new SplashFragment();
    RegisterFragment registerFragment = new RegisterFragment();
    LogInFragment logInFragment = new LogInFragment();

    String register, login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEntryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        register = getIntent().getStringExtra("register");
        login = getIntent().getStringExtra("login");

        if (register == null && login == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.entryActivity, splashFragment).commit();
        }

        if (register != null && register.equals("register")){
            getSupportFragmentManager().beginTransaction().replace(R.id.entryActivity, registerFragment).commit();
        }

        if (login != null && login.equals("login")){
            getSupportFragmentManager().beginTransaction().replace(R.id.entryActivity, logInFragment).commit();
        }

    }
}