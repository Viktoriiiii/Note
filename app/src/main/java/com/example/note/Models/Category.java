package com.example.note.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "Category")
public class Category {

    @PrimaryKey(autoGenerate = true)
    int CategoryID = 0;

    @ColumnInfo(name = "CategoryName")
    String CategoryName = "";

    public int getCategoryID() {
        return CategoryID;
    }

    public void setCategoryID(int categoryID) {
        CategoryID = categoryID;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }
}
