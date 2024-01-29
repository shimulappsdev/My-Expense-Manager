package com.expensemanager.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.expensemanager.R;
import com.expensemanager.activity.ContainerActivity;
import com.expensemanager.adapter.ItemAdapter;
import com.expensemanager.adapter.ListAdapter;
import com.expensemanager.adapter.ListItemAdapter;
import com.expensemanager.databinding.FragmentListViewBinding;
import com.expensemanager.databse.IncomeDatabase;
import com.expensemanager.databse.ListDatabase;
import com.expensemanager.model.Income;
import com.expensemanager.model.ListItem;
import com.expensemanager.model.ParentList;
import com.expensemanager.utils.ItemUpdateListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ListViewFragment extends Fragment implements ItemUpdateListener {

    FragmentListViewBinding binding;
    ItemAdapter itemAdapter;
    Executor executor = Executors.newSingleThreadExecutor();
    int listId;
    String heading, date;

    ItemUpdateListener listener = this;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentListViewBinding.inflate(getLayoutInflater(), container, false);

        listId = getActivity().getIntent().getIntExtra("listId", 0);
        heading = getActivity().getIntent().getStringExtra("heading");
        date = getActivity().getIntent().getStringExtra("date");

        binding.listHeading.setText(heading);
        binding.listDate.setText(date);

        ShowData();

        binding.backBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ContainerActivity.class);
            intent.putExtra("list", "list");
            startActivity(intent);
            getActivity().finish();
        });

        return binding.getRoot();
    }

    private void ShowData() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                ListDatabase listDatabase = ListDatabase.getInstance(getActivity());
                List<ListItem> listItems = listDatabase.listItemDao().getListItemsByParentListId(listId);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        itemAdapter = new ItemAdapter(getActivity(), listItems, listener);
                        binding.listViewRecyclerView.setAdapter(itemAdapter);
                    }
                });
            }
        });
    }

    @Override
    public void ItemAdd(ListItem listItem) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_list_item_layout);

        TextView addListItem = dialog.findViewById(R.id.addListItem);
        EditText listItemName = dialog.findViewById(R.id.listItemName);
        EditText listItemAmount = dialog.findViewById(R.id.listItemAmount);
        EditText listItemQuantity = dialog.findViewById(R.id.listItemQuantity);
        EditText listItemUnit = dialog.findViewById(R.id.listItemUnit);
        CardView insertBtn = dialog.findViewById(R.id.insertBtn);

        addListItem.setText("Add New Item");

        insertBtn.setOnClickListener(view -> {
            String itemName = listItemName.getText().toString();
            String itemAmount = listItemAmount.getText().toString();
            String itemQuantity = listItemQuantity.getText().toString();
            String itemUnit = listItemUnit.getText().toString();
            if (itemName == ""){
                listItemName.setError("Required");
            }else {
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        ListDatabase listDatabase = ListDatabase.getInstance(getActivity());
                        ListItem listItem = new ListItem();
                        listItem.setList_item_name(itemName);
                        listItem.setList_item_amount(Integer.parseInt(itemAmount));
                        listItem.setList_item_quantity(itemQuantity);
                        listItem.setList_item_unit(itemUnit);
                        listItem.setList_item_status("Pending");
                        listItem.setParentListId(listId);
                        listDatabase.listItemDao().addListItem(listItem);
                    }
                });
                ShowData();
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    @Override
    public void ItemUpdate(ListItem listItem) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_list_item_layout);

        TextView addListItem = dialog.findViewById(R.id.addListItem);
        EditText listItemName = dialog.findViewById(R.id.listItemName);
        EditText listItemAmount = dialog.findViewById(R.id.listItemAmount);
        EditText listItemQuantity = dialog.findViewById(R.id.listItemQuantity);
        EditText listItemUnit = dialog.findViewById(R.id.listItemUnit);
        CardView insertBtn = dialog.findViewById(R.id.insertBtn);

        addListItem.setText("Edit The Item");
        listItemName.setText(listItem.getList_item_name());
        listItemAmount.setText(String.valueOf(listItem.getList_item_amount()));
        listItemQuantity.setText(listItem.getList_item_quantity());
        listItemUnit.setText(listItem.getList_item_unit());

        insertBtn.setOnClickListener(view -> {
            String itemName = listItemName.getText().toString();
            String itemAmount = listItemAmount.getText().toString();
            String itemQuantity = listItemQuantity.getText().toString();
            String itemUnit = listItemUnit.getText().toString();
            if (itemName == ""){
                listItemName.setError("Required");
            }else {
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        ListDatabase listDatabase = ListDatabase.getInstance(getActivity());
                        ListItem updateItem = new ListItem();
                        updateItem.setList_item_id(listItem.getList_item_id());
                        updateItem.setList_item_name(itemName);
                        updateItem.setList_item_amount(Integer.parseInt(itemAmount));
                        updateItem.setList_item_quantity(itemQuantity);
                        updateItem.setList_item_unit(itemUnit);
                        updateItem.setList_item_status("Pending");
                        updateItem.setParentListId(listId);
                        listDatabase.listItemDao().updateListItem(updateItem);
                    }
                });
                ShowData();
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    @Override
    public void ItemDelete(ListItem listItem) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                ListDatabase listDatabase = ListDatabase.getInstance(getActivity());
                listDatabase.listItemDao().deleteListItem(listItem);
            }
        });
        ShowData();
    }

    @Override
    public void ItemStatusUpdate(ListItem listItem) {
        final String status = listItem.getList_item_status();
        final String listItemStatus;
        if (status.equals("Pending")) {
            listItemStatus = "Done";
        } else if (status.equals("Done")) {
            listItemStatus = "Pending";
        } else {
            return;
        }
        executor.execute(new Runnable() {
            @Override
            public void run() {
                ListDatabase listDatabase = ListDatabase.getInstance(getActivity());
                ListItem update = new ListItem();
                update.setList_item_id(listItem.getList_item_id());
                update.setList_item_name(listItem.getList_item_name());
                update.setList_item_amount(listItem.getList_item_amount());
                update.setList_item_quantity(listItem.getList_item_quantity());
                update.setList_item_unit(listItem.getList_item_unit());
                update.setList_item_status(listItemStatus);
                update.setParentListId(listItem.getParentListId());
                listDatabase.listItemDao().updateListItem(update);
            }
        });
        ShowData();
    }
}