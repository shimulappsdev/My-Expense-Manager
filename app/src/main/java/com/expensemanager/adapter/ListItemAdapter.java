package com.expensemanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.expensemanager.R;
import com.expensemanager.model.ListItem;
import com.expensemanager.utils.ListItemListener;
import com.expensemanager.viewholder.ListItemViewHolder;

import java.util.List;

public class ListItemAdapter extends RecyclerView.Adapter<ListItemViewHolder> {

    private Context context;
    private List<ListItem> listItems;
    private ListItemListener listener;

    public ListItemAdapter(Context context, List<ListItem> listItems, ListItemListener listener) {
        this.context = context;
        this.listItems = listItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ListItemViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ListItemViewHolder holder, int position) {

        ListItem listItem = listItems.get(position);
        holder.listTitle.setText(listItem.getList_item_name());
        int amount = listItem.getList_item_amount();
        holder.listAmount.setText(String.valueOf(amount));
        holder.itemQuantityUnit.setText(listItem.getList_item_quantity()+ " "+ listItem.getList_item_unit());

        holder.removeItem.setOnClickListener(view -> {
            listener.removeItem(listItem);
        });

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }
}
