package com.expensemanager.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "List_Item")
public class ListItem {

    @PrimaryKey(autoGenerate = true)
    private int list_item_id;

    @ColumnInfo
    private int list_item_amount;

    @ColumnInfo
    private String list_item_name, list_item_status, list_item_quantity, list_item_unit;

    @ColumnInfo(name = "parent_list_id")
    private int parentListId;

    public int getParentListId() {
        return parentListId;
    }

    public void setParentListId(int parentListId) {
        this.parentListId = parentListId;
    }

    public int getList_item_id() {
        return list_item_id;
    }

    public void setList_item_id(int list_item_id) {
        this.list_item_id = list_item_id;
    }

    public int getList_item_amount() {
        return list_item_amount;
    }

    public void setList_item_amount(int list_item_amount) {
        this.list_item_amount = list_item_amount;
    }

    public String getList_item_name() {
        return list_item_name;
    }

    public void setList_item_name(String list_item_name) {
        this.list_item_name = list_item_name;
    }

    public String getList_item_status() {
        return list_item_status;
    }

    public void setList_item_status(String list_item_status) {
        this.list_item_status = list_item_status;
    }

    public String getList_item_quantity() {
        return list_item_quantity;
    }

    public void setList_item_quantity(String list_item_quantity) {
        this.list_item_quantity = list_item_quantity;
    }

    public String getList_item_unit() {
        return list_item_unit;
    }

    public void setList_item_unit(String list_item_unit) {
        this.list_item_unit = list_item_unit;
    }
}
