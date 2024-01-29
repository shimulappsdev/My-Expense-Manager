package com.expensemanager.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.expensemanager.model.Budget;
import com.expensemanager.model.Income;

import java.util.List;

@Dao
public interface BudgetDao {

    @Insert
    void addBudget(Budget budget);

    @Update
    void updateBudget(Budget budget);

    @Delete
    void deleteBudget(Budget budget);

    @Query("SELECT * FROM budget")
    List<Budget> getAllBudget();

    @Query("SELECT * FROM Budget WHERE budget_date = :month")
    List<Budget> getAllBudgetByMonth(String month);

    @Query("SELECT * FROM budget WHERE budget_name == :budget_category")
    List<Budget> getBudgetByCategory(String budget_category);

    @Query("SELECT * FROM Budget WHERE budget_date = :month AND budget_name = :category")
    Budget getBudgetByMonthAndCategory(String month, String category);

    @Query("DELETE FROM budget WHERE budget_date == :previousMonth")
    void deletePreviousMonthBudget(String previousMonth);

    @Query("DELETE FROM budget WHERE budget_date != :currentMonth")
    void deleteAllPreviousMonthBudget(String currentMonth);

}
