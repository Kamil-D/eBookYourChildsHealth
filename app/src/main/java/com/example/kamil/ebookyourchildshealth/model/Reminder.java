package com.example.kamil.ebookyourchildshealth.model;

/**
 * Created by KamilosD on 2016-12-29.
 */

public class Reminder {

    private int id;
    private int visitId;
    private long calendarId;
    private String date;

    public Reminder () {
    }

    public Reminder (int id, int visitId, long calendarId, String date) {
        this.id = id;
        this.visitId = visitId;
        this.calendarId = calendarId;
        this.date = date;
    }

    public long getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(long calendarId) {
        this.calendarId = calendarId;
    }

    public int getVisitId() {
        return visitId;
    }

    public void setVisitId(int visitId) {
        this.visitId = visitId;
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
}
