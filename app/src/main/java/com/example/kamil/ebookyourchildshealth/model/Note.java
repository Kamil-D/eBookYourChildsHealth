package com.example.kamil.ebookyourchildshealth.model;

/**
 * Created by KamilosD on 2016-11-26.
 */

public class Note {

    private int id;
    private int diseaseId;
    private String date;
    private String noteText;

    public Note() {

    }

    public Note(int id, int diseaseId, String date, String noteText) {
        this.id = id;
        this.diseaseId = diseaseId;
        this.date = date;
        this.noteText = noteText;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDiseaseId() {
        return diseaseId;
    }

    public void setDiseaseId(int diseaseId) {
        this.diseaseId = diseaseId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }
}

