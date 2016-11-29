package com.example.kamil.ebookyourchildshealth.model;

/**
 * Created by Kamil on 2016-11-24.
 */

public class DiseaseListItem {

    private int id;
    private String name;
    private String date;

    public DiseaseListItem(int id, String name, String date) {
        this.id = id;
        this.name = name;
        this.date = date;
    }

    public DiseaseListItem(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
