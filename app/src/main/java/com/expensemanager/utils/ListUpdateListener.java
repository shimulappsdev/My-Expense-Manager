package com.expensemanager.utils;

import com.expensemanager.model.ParentList;

public interface ListUpdateListener {
    void listUpdate(ParentList parentList);
    void listDelete(ParentList parentList);
}
