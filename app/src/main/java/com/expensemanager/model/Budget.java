package com.expensemanager.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Budget")
public class Budget {
    @PrimaryKey(autoGenerate = true)
    private int budget_id;

    @ColumnInfo
    private int budget_amount;

    @ColumnInfo
    private String budget_name, budget_date, budget_time;

    @ColumnInfo
    private long budget_timestamp;

    public int getBudget_id() {
        return budget_id;
    }

    public void setBudget_id(int budget_id) {
        this.budget_id = budget_id;
    }

    public int getBudget_amount() {
        return budget_amount;
    }

    public void setBudget_amount(int budget_amount) {
        this.budget_amount = budget_amount;
    }

    public String getBudget_name() {
        return budget_name;
    }

    public void setBudget_name(String budget_name) {
        this.budget_name = budget_name;
    }

    public String getBudget_date() {
        return budget_date;
    }

    public void setBudget_date(String budget_date) {
        this.budget_date = budget_date;
    }

    public String getBudget_time() {
        return budget_time;
    }

    public void setBudget_time(String budget_time) {
        this.budget_time = budget_time;
    }

    public long getBudget_timestamp() {
        return budget_timestamp;
    }

    public void setBudget_timestamp(long budget_timestamp) {
        this.budget_timestamp = budget_timestamp;
    }
}
