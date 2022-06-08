package com.edgarlopez.pizzerialosarcos.model;

public class ExtraIngredient {
    private String id;
    private String title;
    private String food;
    private Integer mPrice;
    private Integer sPrice;
    private Integer bPrice;

    public ExtraIngredient() {

    }

    public ExtraIngredient(String id, String title, String food, Integer mPrice, Integer sPrice, Integer bPrice) {
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

    public Integer getbPrice() {
        return bPrice;
    }

    public void setbPrice(Integer bPrice) {
        this.bPrice = bPrice;
    }
}
