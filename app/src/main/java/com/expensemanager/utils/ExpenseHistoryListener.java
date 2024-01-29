package com.expensemanager.utils;

import com.expensemanager.model.Expense;

public interface ExpenseHistoryListener {
    void deleteExpenseSingleHistory(Expense expense);
}
