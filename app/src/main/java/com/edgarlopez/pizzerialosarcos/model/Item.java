package com.edgarlopez.pizzerialosarcos.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.edgarlopez.pizzerialosarcos.data.DataConverter;

import java.io.Serializable;
import java.util.List;

@Entity(tableName = "item_table")
public class Item {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "isComplete")
    public boolean isComplete;

    @ColumnInfo(name = "extraIngredientList")
    @TypeConverters(DataConverter.class)
    public List<ExtraIngredient> extraIngredientList;

    @ColumnInfo(name = "size")
    public String size;

    @ColumnInfo(name = "amount")
    public int amount;

    @ColumnInfo(name = "comments")
    public String comments;

    @ColumnInfo(name = "price")
    public float price;

    public Item(@NonNull String title, boolean isComplete, List<ExtraIngredient> extraIngredientList,
                @NonNull String size, int amount, @NonNull String comments, float price) {
        this.title = title;
        this.isComplete = isComplete;
        this.extraIngredientList = extraIngredientList;
        this.size = size;
        this.amount = amount;
        this.comments = comments;
        this.price = price;
    }

    @Ignore
    public Item() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ExtraIngredient> getExtraIngredientList() {
        return extraIngredientList;
    }

    public void setExtraIngredientList(List<ExtraIngredient> extraIngredientList) {
        this.extraIngredientList = extraIngredientList;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
