package com.edgarlopez.pizzerialosarcos.model;

import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

public class Food implements Serializable {
    private String id;
    private String title;
    private String description;
    private int bPrice;
    private Integer mPrice;
    private Integer sPrice;
    private int listPosition;

    public Food() {

    }

    public Food(String id, String title, String description, int bPrice, Integer mPrice, Integer sPrice, int listPosition) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.bPrice = bPrice;
        this.mPrice = mPrice;
        this.sPrice = sPrice;
        this.listPosition = listPosition;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getbPrice() {
        return bPrice;
    }

    public void setbPrice(int bPrice) {
        this.bPrice = bPrice;
    }

    public Integer getmPrice() {
        return mPrice;
    }

    public void setmPrice(Integer mPrice) {
        this.mPrice = mPrice;
    }

    public Integer getsPrice() {
        return sPrice;
    }

    public void setsPrice(Integer sPrice) {
        this.sPrice = sPrice;
    }

    public int getListPosition() {
        return listPosition;
    }

    public void setListPosition(int listPosition) {
        this.listPosition = listPosition;
    }
}
