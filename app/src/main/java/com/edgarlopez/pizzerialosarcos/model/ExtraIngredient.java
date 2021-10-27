package com.edgarlopez.pizzerialosarcos.model;

public class ExtraIngredient {
    private String id;
    private String title;
    private String food;
    private int mPrice;
    private int sPrice;
    private int bPrice;

    public ExtraIngredient() {

    }

    public ExtraIngredient(String id, String title, String food, int mPrice, int sPrice, int bPrice) {
        this.id = id;
        this.title = title;
        this.food = food;
        this.mPrice = mPrice;
        this.sPrice = sPrice;
        this.bPrice = bPrice;
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

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
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

    public int getbPrice() {
        return bPrice;
    }

    public void setbPrice(int bPrice) {
        this.bPrice = bPrice;
    }
}
