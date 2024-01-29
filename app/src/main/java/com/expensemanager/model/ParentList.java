package com.expensemanager.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.Relation;

import java.util.List;

@Entity(tableName = "Parent_List")
public class ParentList {

    @PrimaryKey(autoGenerate = true)
    private int list_id;

    @ColumnInfo
    private String list_heading, list_category, list_date;

    @ColumnInfo
    private long time_stamp;

    @Relation(
            parentColumn = "list_id",
            entityColumn = "parent_list_id"
    )

    @Ignore
    private List<ListItem> listItems;

    public ParentList() {
    }

    public ParentList(List<ListItem> listItems) {
        this.listItems = listItems;
    }

    public List<ListItem> getListItems() {
        return listItems;
    }

    public int getList_id() {
        return list_id;
    }

    public void setList_id(int list_id) {
        this.list_id = list_id;
    }

    public String getList_heading() {
        return list_heading;
    }

    public void setList_heading(String list_heading) {
        this.list_heading = list_heading;
    }

    public String getList_category() {
        return list_category;
    }

    public void setList_category(String list_category) {
        this.list_category = list_category;
    }

    public String getList_date() {
        return list_date;
    }

    public void setList_date(String list_date) {
        this.list_date = list_date;
    }

    public long getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(long time_stamp) {
        this.time_stamp = time_stamp;
    }
}
