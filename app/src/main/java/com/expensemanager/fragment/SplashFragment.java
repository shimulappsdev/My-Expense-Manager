package com.expensemanager.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.expensemanager.R;
import com.expensemanager.activity.EntryActivity;
import com.expensemanager.activity.MainActivity;
import com.expensemanager.databinding.FragmentSplashBinding;

public class SplashFragment extends Fragment {

    FragmentSplashBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSplashBinding.inflate(getLayoutInflater(), container, false);

        Thread thread = new Thread(){
            public void run(){
                try {
                    sleep(4000);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    boolean isLoggedIn = preferences.getBoolean("isLoggedIn", false);
                    if (isLoggedIn) {
                        startActivity(new Intent(getActivity(), MainActivity.class));
                        getActivity().finish();
                    } else {
                        Intent intent = new Intent(getActivity(), EntryActivity.class);
                        intent.putExtra("login", "login");
                        startActivity(intent);
                        getActivity().finish();
                    }
                }
            }
        };thread.start();

        return binding.getRoot();
    }
}