package com.expensemanager.utils;

import com.expensemanager.model.ListItem;

public interface ItemUpdateListener {
    void ItemAdd(ListItem listItem);
    void ItemUpdate(ListItem listItem);
    void ItemDelete(ListItem listItem);
    void ItemStatusUpdate(ListItem listItem);
}
