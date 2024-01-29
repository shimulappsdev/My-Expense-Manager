package com.expensemanager.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Income")
public class Income {

    @PrimaryKey(autoGenerate = true)
    private int income_id;

    @ColumnInfo
    private int income_amount;

    @ColumnInfo
    private String income_particular, income_account_type, income_date, income_time, show_date;

    @ColumnInfo
    private long income_timestamp;


    public int getIncome_id() {
        return income_id;
    }

    public void setIncome_id(int income_id) {
        this.income_id = income_id;
    }

    public int getIncome_amount() {
        return income_amount;
    }

    public void setIncome_amount(int income_amount) {
        this.income_amount = income_amount;
    }

    public String getIncome_particular() {
        return income_particular;
    }

    public void setIncome_particular(String income_particular) {
        this.income_particular = income_particular;
    }

    public String getIncome_account_type() {
        return income_account_type;
    }

    public void setIncome_account_type(String income_account_type) {
        this.income_account_type = income_account_type;
    }

    public String getIncome_date() {
        return income_date;
    }

    public void setIncome_date(String income_date) {
        this.income_date = income_date;
    }

    public String getIncome_time() {
        return income_time;
    }

    public void setIncome_time(String income_time) {
        this.income_time = income_time;
    }

    public long getIncome_timestamp() {
        return income_timestamp;
    }

    public void setIncome_timestamp(long income_timestamp) {
        this.income_timestamp = income_timestamp;
    }

    public String getShow_date() {
        return show_date;
    }

    public void setShow_date(String show_date) {
        this.show_date = show_date;
    }

    public Character getProfileLetter(){
        return income_particular.charAt(0);
    }
}
