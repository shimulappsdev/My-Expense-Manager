package com.expensemanager.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.expensemanager.R;
import com.expensemanager.activity.ContainerActivity;
import com.expensemanager.databse.ListDatabase;
import com.expensemanager.model.ListItem;
import com.expensemanager.model.ParentList;
import com.expensemanager.utils.ListUpdateListener;
import com.expensemanager.viewholder.ListViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ListAdapter extends RecyclerView.Adapter<ListViewHolder> {

    private Context context;
    private List<ParentList> listItems;
    private ListUpdateListener listener;

    public ListAdapter(Context context, List<ParentList> listItems, ListUpdateListener listener) {
        this.context = context;
        this.listItems = listItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ListViewHolder(LayoutInflater.from(context).inflate(R.layout.list_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        ParentList parentList = listItems.get(position);
        holder.listHeading.setText(parentList.getList_heading());
        holder.category.setText(parentList.getList_category());
        holder.listDate.setText(parentList.getList_date());

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                ListDatabase listDatabase = ListDatabase.getInstance(context);
                List<ListItem> listItems = listDatabase.listItemDao().getListItemsByParentListId(parentList.getList_id());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (listItems != null){
                            ItemForListAdapter itemForListAdapter = new ItemForListAdapter(context, listItems);
                            holder.itemForLisLayout.setAdapter(itemForListAdapter);
                        }
                    }
                });
            }
        });

        holder.optionBtn.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(context, view);
            popupMenu.inflate(R.menu.option_menu);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.optionEdit:
                            listener.listUpdate(parentList);
                            break;

                        case R.id.optionDelete:
                            DeleteList();
                            break;
                    }
                    return true;
                }

                private void DeleteList() {
                    final AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    View mView = LayoutInflater.from(context).inflate(R.layout.delete_layout, null);
                    alert.setView(mView);
                    final AlertDialog alertDialog = alert.create();
                    alertDialog.setCancelable(false);
                    mView.findViewById(R.id.chancelBTN).setOnClickListener(v -> {
                        alertDialog.dismiss();
                        popupMenu.dismiss();
                    });
                    mView.findViewById(R.id.okBTN).setOnClickListener(v -> {
                        listener.listDelete(parentList);
                        alertDialog.dismiss();
                        popupMenu.dismiss();
                    });
                    alertDialog.show();
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                }
            });
            popupMenu.show();
        });

        holder.itemCard.setOnClickListener(view -> {
            Intent intent = new Intent(context, ContainerActivity.class);
            intent.putExtra("listId", parentList.getList_id());
            intent.putExtra("heading", parentList.getList_heading());
            intent.putExtra("date", parentList.getList_date());
            intent.putExtra("listView", "listView");
            context.startActivity(intent);
        });
    }
    private void runOnUiThread(Runnable action) {
        if (context instanceof Activity) {
            ((Activity) context).runOnUiThread(action);
        }
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }
}
