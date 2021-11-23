package com.edgarlopez.pizzerialosarcos.model;

import java.util.List;

public class Order {
    private String folio;
    private String client;
    private String clientName;
    private boolean complete;
    private String status;
    private long date;
    private String location;
    private float totalPrice;
    private int items;
    private List<Item> itemList;

    public Order() {
    }

    public Order(String folio, String client, String clientName, boolean complete, String status, long date, String location, float totalPrice, int items, List<Item> itemList) {
        this.folio = folio;
        this.client = client;
        this.clientName = clientName;
        this.complete = complete;
        this.status = status;
        this.date = date;
        this.location = location;
        this.totalPrice = totalPrice;
        this.items = items;
        this.itemList = itemList;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
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
