package com.expensemanager.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Expense")
public class Expense {

    @PrimaryKey(autoGenerate = true)
    private int expense_id;

    @ColumnInfo
    private int expense_amount;

    @ColumnInfo
    private String expense_particular, expense_category, expense_date, expense_time, expense_month, show_date;

    @ColumnInfo
    private long expense_timeStamp;

    public int getExpense_id() {
        return expense_id;
    }

    public void setExpense_id(int expense_id) {
        this.expense_id = expense_id;
    }

    public int getExpense_amount() {
        return expense_amount;
    }

    public void setExpense_amount(int expense_amount) {
        this.expense_amount = expense_amount;
    }

    public String getExpense_particular() {
        return expense_particular;
    }

    public void setExpense_particular(String expense_particular) {
        this.expense_particular = expense_particular;
    }

    public String getExpense_category() {
        return expense_category;
    }

    public void setExpense_category(String expense_category) {
        this.expense_category = expense_category;
    }

    public String getExpense_date() {
        return expense_date;
    }

    public void setExpense_date(String expense_date) {
        this.expense_date = expense_date;
    }

    public String getExpense_time() {
        return expense_time;
    }

    public void setExpense_time(String expense_time) {
        this.expense_time = expense_time;
    }

    public String getExpense_month() {
        return expense_month;
    }

    public void setExpense_month(String expense_month) {
        this.expense_month = expense_month;
    }

    public long getExpense_timeStamp() {
        return expense_timeStamp;
    }

    public void setExpense_timeStamp(long expense_timeStamp) {
        this.expense_timeStamp = expense_timeStamp;
    }

    public String getShow_date() {
        return show_date;
    }

    public void setShow_date(String show_date) {
        this.show_date = show_date;
    }

    public Character getProfileLetter(){
        return expense_particular.charAt(0);
    }

}
