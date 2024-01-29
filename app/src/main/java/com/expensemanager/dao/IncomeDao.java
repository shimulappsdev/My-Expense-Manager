package com.expensemanager.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.expensemanager.model.Income;

import java.util.List;

@Dao
public interface IncomeDao {

    @Insert
    void addIncome(Income income);

    @Update
    void updateIncome(Income income);

    @Delete
    void deleteIncome(Income income);

    @Query("SELECT * FROM Income")
    List<Income> getAllIncome();

    @Query("DELETE FROM income WHERE income_date == :previousMonth")
    void deletePreviousMonthIncome(String previousMonth);

    @Query("DELETE FROM income WHERE income_date != :currentMonth")
    void deleteAllPreviousMonthIncome(String currentMonth);

    @Query("SELECT * FROM income WHERE income_date == :particularDate")
    List<Income> getIncomeByMonth(String particularDate);

}
