package com.expensemanager.utils;

import com.expensemanager.model.Income;

public interface IncomeUpdateListener {
    void incomeItemDelete(Income income);
    void incomeItemEdit(Income income);
}
