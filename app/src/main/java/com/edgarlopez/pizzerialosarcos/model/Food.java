package com.edgarlopez.pizzerialosarcos.model;

import java.io.Serializable;

public class Food implements Serializable {
    private String id;
    private String title;
    private String description;
    private int bPrice;
    private int mPrice;
    private int sPrice;

    public Food() {

    }

    public Food(String id, String title, String description, int bPrice, int mPrice, int sPrice) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.bPrice = bPrice;
        this.mPrice = mPrice;
        this.sPrice = sPrice;
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

    public int getmPrice() {
        return mPrice;
    }

    public void setmPrice(int mPrice) {
        this.mPrice = mPrice;
    }

    public int getsPrice() {
        return sPrice;
    }

    public void setsPrice(int sPrice) {
        this.sPrice = sPrice;
    }
}
