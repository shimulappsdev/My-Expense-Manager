package com.expensemanager.adapter;

import android.content.Context;
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
import com.expensemanager.model.ListItem;
import com.expensemanager.utils.ItemUpdateListener;
import com.expensemanager.viewholder.ItemViewHolder;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemViewHolder> {
    private Context context;
    private List<ListItem> listItems;
    private ItemUpdateListener listener;

    public ItemAdapter(Context context, List<ListItem> listItems, ItemUpdateListener listener) {
        this.context = context;
        this.listItems = listItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        ListItem listItem = listItems.get(position);
        holder.listTitle.setText(listItem.getList_item_name());
        holder.itemQuantityUnit.setText(listItem.getList_item_quantity()+ " "+ listItem.getList_item_unit());
        holder.listAmount.setText(String.valueOf(listItem.getList_item_amount()));
        holder.statusBtn.setText(listItem.getList_item_status());

        if (listItem.getList_item_status().equals("Done")){
            holder.statusBtn.setBackgroundResource(R.drawable.status_back1);
        }
        if (listItem.getList_item_status().equals("Pending")){
            holder.statusBtn.setBackgroundResource(R.drawable.status_back2);
        }

        holder.itemOptionBtn.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(context, view);
            popupMenu.inflate(R.menu.list_option_menu);

            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    switch (item.getItemId()){
                        case R.id.listOptionAdd:
                            listener.ItemAdd(listItem);
                            break;

                        case R.id.listOptionEdit:
                            listener.ItemUpdate(listItem);
                            break;

                        case R.id.listOptionDelete:
                            DeleteLisItem();
                            break;
                    }

                    return true;
                }

                private void DeleteLisItem() {
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
                        listener.ItemDelete(listItem);
                        alertDialog.dismiss();
                        popupMenu.dismiss();
                    });
                    alertDialog.show();
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                }
            });

            popupMenu.show();
        });

        holder.statusBtn.setOnClickListener(view -> {
            listener.ItemStatusUpdate(listItem);
        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }
}
