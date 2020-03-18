package com.example.healthierexpert;

import java.io.Serializable;

public class Medicine implements Serializable
{
    private String noteTitle, checkedBoxes;
    private boolean isChecked = false;

    public Medicine(String noteTitle, String checkedBoxes) {
        this.noteTitle = noteTitle;
        this.checkedBoxes = checkedBoxes;
    }

    String getNoteTitle()
    {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle)
    {
        this.noteTitle = noteTitle;
    }

    boolean isChecked()
    {
        return isChecked;
    }

    void setChecked(boolean isChecked)
    {
        this.isChecked = isChecked;
    }

    public String getCheckedBoxes() {
        return checkedBoxes;
    }

    public void setCheckedBoxes(String checkedBoxes) {
        this.checkedBoxes = checkedBoxes;
    }
}
