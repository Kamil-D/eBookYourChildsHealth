package com.example.kamil.ebookyourchildshealth.model;

/**
 * Created by KamilosD on 2016-12-29.
 */

public class Reminder {

    private int id;
    private int visitId;
    private long calendarId;

    public Reminder () {
    }

    public Reminder (int id, int visitId, long calendarId) {
        this.id = id;
        this.visitId = visitId;
        this.calendarId = calendarId;
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
}
