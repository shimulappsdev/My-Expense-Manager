package com.expensemanager.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.expensemanager.activity.ContainerActivity;
import com.expensemanager.activity.MainActivity;
import com.expensemanager.adapter.IncomeAdapter;
import com.expensemanager.adapter.ListAdapter;
import com.expensemanager.databinding.FragmentListBinding;
import com.expensemanager.databse.IncomeDatabase;
import com.expensemanager.databse.ListDatabase;
import com.expensemanager.model.Income;
import com.expensemanager.model.ListItem;
import com.expensemanager.model.ParentList;
import com.expensemanager.utils.ListUpdateListener;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ListFragment extends Fragment implements ListUpdateListener {

    FragmentListBinding binding;
    ListAdapter listAdapter;
    Executor executor = Executors.newSingleThreadExecutor();
    ListUpdateListener listener = this;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentListBinding.inflate(getLayoutInflater(), container, false);


        binding.addListBtn.setOnClickListener(view -> {
            Date currentDateAndTime = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String listDate = dateFormat.format(currentDateAndTime);
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    ListDatabase listDatabase = ListDatabase.getInstance(getActivity());
                    ParentList parentList = new ParentList(new ArrayList<>());
                    parentList.setList_heading("blank");
                    parentList.setList_category("blank");
                    parentList.setList_date(listDate);
                    parentList.setTime_stamp(System.currentTimeMillis());
                    listDatabase.parentListDao().addParentList(parentList);

                    Intent intent = new Intent(getActivity(), ContainerActivity.class);
                    intent.putExtra("preparingList", "preparingList");
                    startActivity(intent);
                }
            });
        });

        binding.addListBtn.getDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

        binding.backBtn.setOnClickListener(view -> {
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        });

        ShowData();

        return binding.getRoot();
    }

    private void ShowData() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                ListDatabase listDatabase = ListDatabase.getInstance(getActivity());
                List<ParentList> parentLists = listDatabase.parentListDao().getAllParentLists();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (parentLists != null){
                            listAdapter = new ListAdapter(getActivity(), parentLists, listener);
                            binding.listRecyclerView.setAdapter(listAdapter);
                        }
                    }
                });
            }
        });
    }

    @Override
    public void listUpdate(ParentList parentList) {
        Intent intent = new Intent(getActivity(), ContainerActivity.class);
        intent.putExtra("editList", "editList");
        intent.putExtra("listId", parentList.getList_id());
        intent.putExtra("heading", parentList.getList_heading());
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void listDelete(ParentList parentList) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                ListDatabase listDatabase = ListDatabase.getInstance(getActivity());
                listDatabase.parentListDao().deleteParentList(parentList);
                listDatabase.parentListDao().getAllBlankParentLists();
                listDatabase.listItemDao().deleteAll(parentList.getList_id());
            }
        });
        ShowData();
    }
}