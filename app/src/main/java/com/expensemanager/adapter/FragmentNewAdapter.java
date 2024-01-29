package com.expensemanager.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.expensemanager.fragment.EducationFragment;
import com.expensemanager.fragment.FoodFragment;
import com.expensemanager.fragment.HousingFragment;
import com.expensemanager.fragment.ShoppingFragment;
import com.expensemanager.fragment.TransportFragment;
import com.expensemanager.fragment.UtilitiesFragment;

public class FragmentNewAdapter extends FragmentStatePagerAdapter {
    String[] name = {"Food", "Housing","Education", "Shopping", "Transport", "Utilities"};
    public FragmentNewAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){

            case 0:
                return new FoodFragment();
            case 1:
                return new HousingFragment();
            case 2:
                return new EducationFragment();
            case 3:
                return new ShoppingFragment();
            case 4:
                return new TransportFragment();
            case 5:
                return new UtilitiesFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return name.length;
    }
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return name[position];
    }
}
