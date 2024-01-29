package com.expensemanager.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.expensemanager.R;
import com.expensemanager.activity.ContainerActivity;
import com.expensemanager.adapter.ListItemAdapter;
import com.expensemanager.databinding.FragmentEditListBinding;
import com.expensemanager.databse.ListDatabase;
import com.expensemanager.model.ListItem;
import com.expensemanager.model.ParentList;
import com.expensemanager.utils.ListItemListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class EditListFragment extends Fragment implements ListItemListener {

    FragmentEditListBinding binding;
    ListItemAdapter listItemAdapter;
    ListItemListener listener = this;
    String item, heading;
    int list_id;
    Executor executor = Executors.newSingleThreadExecutor();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditListBinding.inflate(getLayoutInflater(), container, false);

        binding.listCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                item = adapterView.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        ShowData();

        list_id = getActivity().getIntent().getIntExtra("listId", 0);
        heading = getActivity().getIntent().getStringExtra("heading");
        binding.listHeading.setText(heading);

        ArrayList<String> categories = new ArrayList<>();
        categories.add("-select-");
        categories.add("Food");
        categories.add("Daily Bazar");
        categories.add("Housing");
        categories.add("Education");
        categories.add("Shopping");
        categories.add("Others");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.list_category_iten_layout, categories);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        binding.listCategory.setAdapter(adapter);

        binding.insertItemBtn.setOnClickListener(view -> {
            String listHeading = binding.listHeading.getText().toString();
            String listCategory = item;
            String listItemName = binding.listItemName.getText().toString();
            String listItemAmount = binding.listItemAmount.getText().toString();
            String listItemQuantity = binding.listItemQuantity.getText().toString();
            String listItemUnit = binding.listItemUnit.getText().toString();
            String listItemStatus = "Pending";

            if (listHeading.equals("")){
                binding.listHeading.setError("Required");
            }else if (listCategory.equals("-select-")){
                Toast.makeText(getActivity(), "Please Select Category", Toast.LENGTH_SHORT).show();
            }else {
                if (listItemName.equals("")){
                    binding.listItemName.setError("Required");
                }else {
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            ListDatabase listDatabase = ListDatabase.getInstance(getActivity());
                            ParentList lastParentList = listDatabase.parentListDao().getLastParentList();

                            String date = lastParentList.getList_date();
                            long timeStamp = lastParentList.getTime_stamp();

                            ParentList parentList = new ParentList(new ArrayList<>());
                            parentList.setList_id(list_id);
                            parentList.setList_heading(listHeading);
                            parentList.setList_category(listCategory);
                            parentList.setList_date(date);
                            parentList.setTime_stamp(timeStamp);
                            listDatabase.parentListDao().updateParentList(parentList);

                            ListItem listItem = new ListItem();
                            listItem.setList_item_name(listItemName);
                            listItem.setList_item_amount(Integer.parseInt(listItemAmount));
                            listItem.setList_item_quantity(listItemQuantity);
                            listItem.setList_item_unit(listItemUnit);
                            listItem.setList_item_status(listItemStatus);
                            listItem.setParentListId(list_id);
                            listDatabase.listItemDao().addListItem(listItem);
                        }
                    });
                    ShowData();
                }
            }
        });

        binding.listDoneBtn.setOnClickListener(view -> {
            String listHeading = binding.listHeading.getText().toString();
            String listCategory = item;
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    ListDatabase listDatabase = ListDatabase.getInstance(getActivity());
                    ParentList lastParentList = listDatabase.parentListDao().getLastParentList();

                    String category = lastParentList.getList_category();
                    String date = lastParentList.getList_date();
                    long timeStamp = lastParentList.getTime_stamp();

                    ParentList parentList = new ParentList(new ArrayList<>());
                    parentList.setList_id(list_id);
                    parentList.setList_heading(listHeading);
                    if (listCategory.equals("-select-")){
                        parentList.setList_category(category);
                    }else {
                        parentList.setList_category(listCategory);
                    }
                    parentList.setList_date(date);
                    parentList.setTime_stamp(timeStamp);
                    listDatabase.parentListDao().updateParentList(parentList);
                }
            });
            Intent intent = new Intent(getActivity(), ContainerActivity.class);
            intent.putExtra("list", "list");
            startActivity(intent);
            getActivity().finish();
        });

        binding.backBtn.setOnClickListener(view -> {
            getActivity().finish();
        });

        return binding.getRoot();
    }
    private void ShowData() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                ListDatabase listDatabase = ListDatabase.getInstance(getActivity());
                List<ListItem> listItems = listDatabase.listItemDao().getListItemsByParentListId(list_id);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listItemAdapter = new ListItemAdapter(getActivity(), listItems, listener);
                        binding.itemRecyclerView.setAdapter(listItemAdapter);

                        binding.listItemName.setText("");
                        binding.listItemAmount.setText("");
                        binding.listItemQuantity.setText("");
                        binding.listItemUnit.setText("");
                    }
                });
            }
        });
    }

    @Override
    public void removeItem(ListItem listItem) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                ListDatabase listDatabase = ListDatabase.getInstance(getActivity());
                listDatabase.listItemDao().deleteListItem(listItem);
            }
        });
        ShowData();
    }
}