package com.expensemanager.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.expensemanager.model.ListItem;

import java.util.List;

@Dao
public interface ListItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addListItem (ListItem listItem);

    @Update
    void updateListItem(ListItem listItem);

    @Delete
    void deleteListItem(ListItem listItem);

    @Query("DELETE FROM LIST_ITEM WHERE parent_list_id = :parentId")
    void deleteAll(int parentId);

    @Query("SELECT * FROM List_Item WHERE parent_list_id = :parentListId")
    List<ListItem> getListItemsByParentListId(int parentListId);

}
