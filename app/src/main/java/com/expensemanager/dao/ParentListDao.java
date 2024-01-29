package com.expensemanager.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.expensemanager.model.ParentList;

import java.util.List;

@Dao
public interface ParentListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addParentList(ParentList parentList);

    @Update
    void updateParentList(ParentList parentList);

    @Delete
    void deleteParentList(ParentList parentList);

    @Transaction
    @Query("DELETE FROM Parent_List WHERE list_heading =='blank'")
    Void getAllBlankParentLists();

    @Transaction
    @Query("SELECT * FROM Parent_List WHERE list_heading != 'blank'")
    List<ParentList> getAllParentLists();

    @Transaction
    @Query("SELECT * FROM Parent_List ORDER BY list_id DESC LIMIT 1")
    ParentList getLastParentList();

}
