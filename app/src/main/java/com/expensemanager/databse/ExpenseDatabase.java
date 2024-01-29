package com.expensemanager.databse;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.expensemanager.dao.ExpenseDao;
import com.expensemanager.model.Expense;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Database(entities = {Expense.class}, version = 1)
public abstract class ExpenseDatabase extends RoomDatabase {

    public abstract ExpenseDao expenseDao();
    private static volatile ExpenseDatabase instance;
    public static synchronized ExpenseDatabase getInstance(Context context){

        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            ExpenseDatabase.class, "ExpenseDB")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

}
