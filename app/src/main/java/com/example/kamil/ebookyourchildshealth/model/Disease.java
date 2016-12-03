package com.example.kamil.ebookyourchildshealth.model;

/**
 * Created by Kamil on 2016-11-24.
 */

public class Disease {

    private int id;
    private int childId;
    private String name;
    private String date;

    public Disease() {

    }

    public Disease(int id, String name, String date) {
        this.id = id;
        this.name = name;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getChildId() {
        return childId;
    }

    public void setChildId(int childId) {
        this.childId = childId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
