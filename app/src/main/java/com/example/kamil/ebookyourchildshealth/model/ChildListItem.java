package com.example.kamil.ebookyourchildshealth.model;

/**
 * Created by Kamil on 2016-11-24.
 */

public class ChildListItem {

    private int id;
    private String name;
    private String imageUri;

    public ChildListItem(int id, String name, String imageUri) {
        this.id = id;
        this.name = name;
        this.imageUri = imageUri;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

}
