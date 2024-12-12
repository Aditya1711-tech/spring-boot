package com.ess.api.request;

public class AddNote {
    private String note;

    public AddNote() {
        super();
    }

    public AddNote(String note) {
        this.note = note;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "AddNote{" +
                "note='" + note + '\'' +
                '}';
    }
}
