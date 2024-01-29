package com.expensemanager.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.expensemanager.model.Expense;
import com.expensemanager.model.Income;

import java.util.List;

@Dao
public interface ExpenseDao {

    @Insert
    void addExpense(Expense expense);

    @Update
    void updateExpense(Expense expense);

    @Delete
    void deleteExpense(Expense expense);

    @Query("SELECT * FROM Expense")
    List<Expense> getAllExpense();

    @Query("SELECT * FROM expense WHERE expense_date == :month")
    List<Expense> getAllExpenseByMonth(String month);

    @Query("SELECT * FROM expense WHERE show_date == :today")
    List<Expense> getAllExpenseOfToday(String today);

    @Query("SELECT * FROM expense WHERE expense_month == :month")
    List<Expense> getAllExpenseByEachMonth(String month);

    @Query("SELECT * FROM expense WHERE expense_date == :particularMonth AND expense_category ==:category")
    List<Expense> getAllExpenseByParticularMonthAndCategory(String particularMonth, String category);

    @Query("DELETE FROM expense WHERE expense_date == :previousMonth")
    void deletePreviousMonthExpense(String previousMonth);

    @Query("DELETE FROM expense WHERE expense_date != :currentMonth")
    void deleteAllPreviousMonthExpense(String currentMonth);
}
