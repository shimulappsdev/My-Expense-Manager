package com.expensemanager.databse;

import android.content.Context;
import android.os.Looper;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.expensemanager.dao.IncomeDao;
import com.expensemanager.model.Income;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Handler;

@Database(entities = {Income.class}, version = 1)
public abstract class IncomeDatabase extends RoomDatabase {

    public abstract IncomeDao incomeDao();
    private static volatile IncomeDatabase instance;
    public static synchronized IncomeDatabase getInstance(Context context){

        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                     IncomeDatabase.class, "IncomeDB")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
