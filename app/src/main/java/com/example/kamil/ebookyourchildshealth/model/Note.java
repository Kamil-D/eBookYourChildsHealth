package com.example.kamil.ebookyourchildshealth.model;

/**
 * Created by KamilosD on 2016-11-26.
 */

public class Note {

    private int diseaseId;
    private String noteText;

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

