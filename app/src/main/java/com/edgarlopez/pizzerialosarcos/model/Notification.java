package com.edgarlopez.pizzerialosarcos.model;

import java.io.Serializable;

public class Notification implements Serializable {
    public String folio;
    public String imageURL;
    public String title;
    public String description;
    public String options;
    public String userToken;
    public boolean viewed;
    public double dateSend;

    public Notification() { }

    public Notification(String folio, String imageURL, String title, String description, String options, String userToken, boolean viewed, double dateSend) {
        this.folio = folio;
        this.imageURL = imageURL;
        this.title = title;
        this.description = description;
        this.options = options;
        this.userToken = userToken;
        this.viewed = viewed;
        this.dateSend = dateSend;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
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

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public boolean isViewed() {
        return viewed;
    }

    public void setViewed(boolean viewed) {
        this.viewed = viewed;
    }

    public double getDateSend() {
        return dateSend;
    }

    public void setDateSend(double dateSend) {
        this.dateSend = dateSend;
    }
}
