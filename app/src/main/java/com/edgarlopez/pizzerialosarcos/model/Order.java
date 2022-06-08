package com.edgarlopez.pizzerialosarcos.model;

import java.util.List;

public class Order {
    private String folio;
    private String client;
    private String clientName;
    private boolean complete;
    private String status;
    private double dateRequest;
    private double dateEstimatedDelivery;
    private double dateProcessed;
    private double dateFinished;
    private double dateDelivered;
    private double dateCanceled;
    private String location;
    private double totalPrice;
    private int items;
    private List<Item> itemList;

    public Order() {
    }

    public Order(String folio, String client, String clientName, boolean complete, String status, double dateRequest, double dateEstimatedDelivery, double dateProcessed, double dateFinished, double dateDelivered, double dateCanceled, String location, double totalPrice, int items, List<Item> itemList) {
        this.folio = folio;
        this.client = client;
        this.clientName = clientName;
        this.complete = complete;
        this.status = status;
        this.dateRequest = dateRequest;
        this.dateEstimatedDelivery = dateEstimatedDelivery;
        this.dateProcessed = dateProcessed;
        this.dateFinished = dateFinished;
        this.dateDelivered = dateDelivered;
        this.dateCanceled = dateCanceled;
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

    public double getDateRequest() {
        return dateRequest;
    }

    public void setDateRequest(double dateRequest) {
        this.dateRequest = dateRequest;
    }

    public double getDateEstimatedDelivery() {
        return dateEstimatedDelivery;
    }

    public void setDateEstimatedDelivery(double dateEstimatedDelivery) {
        this.dateEstimatedDelivery = dateEstimatedDelivery;
    }

    public double getDateProcessed() {
        return dateProcessed;
    }

    public void setDateProcessed(double dateProcessed) {
        this.dateProcessed = dateProcessed;
    }

    public double getDateFinished() {
        return dateFinished;
    }

    public void setDateFinished(double dateFinished) {
        this.dateFinished = dateFinished;
    }

    public double getDateDelivered() {
        return dateDelivered;
    }

    public void setDateDelivered(double dateDelivered) {
        this.dateDelivered = dateDelivered;
    }

    public double getDateCanceled() {
        return dateCanceled;
    }

    public void setDateCanceled(double dateCanceled) {
        this.dateCanceled = dateCanceled;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
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
