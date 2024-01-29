package com.expensemanager.utils;

import com.expensemanager.model.Budget;

public interface BudgetHistoryListener {
    void deleteBudgetSingleHistory(Budget budget);
}
