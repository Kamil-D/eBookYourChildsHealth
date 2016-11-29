package com.example.kamil.ebookyourchildshealth.model;

/**
 * Created by Kamil on 2016-11-24.
 */

public class VisitListItem {

    private int id;
    private String name;
    private String disease;
    private String date;

    public VisitListItem (int id, String name, String disease, String date) {
        this.id = id;
        this.name = name;
        this.disease = disease;
        this.date = date;
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

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
