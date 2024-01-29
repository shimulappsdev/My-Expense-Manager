package com.expensemanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.expensemanager.R;
import com.expensemanager.model.ListItem;
import com.expensemanager.viewholder.ItemForListViewHolder;

import java.util.List;

public class ItemForListAdapter extends RecyclerView.Adapter<ItemForListViewHolder> {
    private Context context;
    private List<ListItem> listItemList;

    public ItemForListAdapter(Context context, List<ListItem> listItemList) {
        this.context = context;
        this.listItemList = listItemList;
    }

    @NonNull
    @Override
    public ItemForListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemForListViewHolder(LayoutInflater.from(context).inflate(R.layout.item_for_list_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemForListViewHolder holder, int position) {
        ListItem listItem = listItemList.get(position);
        holder.itemForListTextView.setText(listItem.getList_item_name());
    }

    @Override
    public int getItemCount() {
        return listItemList.size();
    }
}
