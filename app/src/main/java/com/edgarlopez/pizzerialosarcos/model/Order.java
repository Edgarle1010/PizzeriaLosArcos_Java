package com.edgarlopez.pizzerialosarcos.model;

import java.util.List;

public class Order {
    private String client;
    private String clientName;
    private boolean complete;
    private float date;
    private String location;
    private float totalPrice;
    private int items;
    private List<Item> itemList;

    public Order() {
    }

    public Order(String client, String clientName, boolean complete, float date, String location, float totalPrice, int items, List<Item> itemList) {
        this.client = client;
        this.clientName = clientName;
        this.complete = complete;
        this.date = date;
        this.location = location;
        this.totalPrice = totalPrice;
        this.items = items;
        this.itemList = itemList;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public float getDate() {
        return date;
    }

    public void setDate(float date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getItems() {
        return items;
    }

    public void setItems(int items) {
        this.items = items;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }
}
