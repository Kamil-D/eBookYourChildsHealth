package com.example.kamil.ebookyourchildshealth.model;

/**
 * Created by KamilosD on 2016-11-26.
 */

public class Note {

    private int id;
    private int diseaseId;
    private String noteText;

    public Note() {

    }

    public Note(int id, int diseaseId, String noteText) {
        this.id = id;
        this.diseaseId = diseaseId;
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

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }
}

