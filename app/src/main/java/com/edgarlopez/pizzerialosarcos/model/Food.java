package com.edgarlopez.pizzerialosarcos.model;

public class Food {
    private String id;
    private String title;
    private String description;

    public Food() {

    }

    public Food(String id, String name, String description) {
        this.id = id;
        this.title = name;
        this.description = description;
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

    public void setTitle(String name) {
        this.title = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
