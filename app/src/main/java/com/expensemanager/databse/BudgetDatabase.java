package com.expensemanager.databse;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.expensemanager.dao.BudgetDao;
import com.expensemanager.model.Budget;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Database(entities = {Budget.class}, version = 1)
public abstract class BudgetDatabase extends RoomDatabase {

    public abstract BudgetDao budgetDao();
    public static volatile BudgetDatabase instance;
    public static synchronized  BudgetDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                     BudgetDatabase.class, "BudgetDB")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
