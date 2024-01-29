package com.expensemanager.utils;

import com.expensemanager.model.Expense;

public interface ExpenseUpdateListener {
    void ExpenseUpdate(Expense expense);
    void ExpenseDelete(Expense expense);
}
