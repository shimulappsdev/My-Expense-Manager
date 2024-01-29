package com.expensemanager.databse;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.expensemanager.dao.ListItemDao;
import com.expensemanager.dao.ParentListDao;
import com.expensemanager.model.ListItem;
import com.expensemanager.model.ParentList;

@Database(entities = {ParentList.class, ListItem.class}, version = 1)
public abstract class ListDatabase extends RoomDatabase {

    public abstract ParentListDao parentListDao();
    public abstract ListItemDao listItemDao();
    private static volatile ListDatabase instance;
    public static synchronized ListDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), ListDatabase.class, "ListDB")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

}
