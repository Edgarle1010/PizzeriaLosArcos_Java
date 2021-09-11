package com.edgarlopez.pizzerialosarcos.model;

import java.util.List;

public class Order {
    public String title;
    public boolean isComplete;
    public List<ExtraIngredient> extraIngredientList;
    public String size;
    public int amount;
    public String comments;
    public float price;

    public Order() {
    }

    public Order(String title, boolean isComplete, List<ExtraIngredient> extraIngredientList, String size, int amount, String comments, float price) {
        this.title = title;
        this.isComplete = isComplete;
        this.extraIngredientList = extraIngredientList;
        this.size = size;
        this.amount = amount;
        this.comments = comments;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
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
